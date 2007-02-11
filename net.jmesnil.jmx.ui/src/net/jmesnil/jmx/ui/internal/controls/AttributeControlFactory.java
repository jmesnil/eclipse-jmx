/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package net.jmesnil.jmx.ui.internal.controls;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class AttributeControlFactory {

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static Control createControl(final Composite parent,
            final Object value) {
        if (value != null && value.getClass().isArray()) {
            return createArrayControl(parent, value);
        }
        return null;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static Control createControl(final Composite parent,
            FormToolkit toolkit, final MBeanAttributeInfoWrapper wrapper,
            final IWritableAttributeHandler handler) {
        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        boolean writable = attrInfo.isWritable();

        Object value = null;
        String errorMessage = null;
        try {
            value = wrapper.getValue();
        } catch (Throwable t) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
                    Messages.MBeanAttributeValue_Warning, attrInfo.getName()),
                    t);
            errorMessage = t.getCause().getMessage();
        }

        if (value != null && value instanceof Boolean) {
            return createBooleanControl(parent, toolkit, writable, value,
                    handler);
        }
        if (value != null && value.getClass().isArray()) {
            return createArrayControl(parent, toolkit, value);
        }
        if (value != null && value instanceof CompositeData) {
            return createCompositeDataControl(parent, toolkit,
                    (CompositeData) value);
        }
        if (value != null && value instanceof TabularData) {
            return createTabularDataControl(parent, toolkit,
                    (TabularData) value);
        }
        if (value != null && value instanceof Collection) {
            return createCollectionControl(parent, toolkit, (Collection) value);
        }
        if (value != null && value instanceof Map) {
            return createMapControl(parent, toolkit, (Map) value);
        }
        return createText(parent, toolkit, attrInfo, value, errorMessage,
                handler);
    }

    private static Control createText(final Composite parent,
            FormToolkit toolkit, final MBeanAttributeInfo attrInfo,
            Object value, String errorMessage,
            final IWritableAttributeHandler handler) {

        DecoratedField field = new DecoratedField(parent, SWT.MULTI | SWT.WRAP,
                new ToolkitTextControlCreator(toolkit));
        field.getLayoutControl().setLayoutData(
                new TableWrapData(TableWrapData.FILL_GRAB));
        final Text text = (Text) field.getControl();

        if (errorMessage != null) {
            FieldDecorationRegistry registry = FieldDecorationRegistry
                    .getDefault();
            FieldDecoration errorDecoration = registry
                    .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
            errorDecoration.setDescription(errorMessage);
            field.addFieldDecoration(errorDecoration, SWT.LEFT | SWT.BOTTOM,
                    false);
            text.setText(Messages.unavailable);
            text.setForeground(parent.getDisplay()
                    .getSystemColor(SWT.COLOR_RED));
            return text;
        }

        String attrValue = ""; //$NON-NLS-1$
        try {
            attrValue = StringUtils.toString(value, true);
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
                    Messages.MBeanAttributeValue_Warning, attrInfo.getName()),
                    e);
            attrValue = Messages.unavailable;
        }
        text.setText(attrValue);

        if (!attrInfo.isWritable()) {
            text.setEditable(false);
            text.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLACK));
            return text;
        }

        text.setEditable(true);
        text.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        text.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event event) {
                try {
                    Object newValue = MBeanUtils.getValue(text.getText(),
                            attrInfo.getType());
                    handler.write(newValue);
                } catch (Exception e) {
                    MessageDialog.openError(parent.getShell(),
                            Messages.AttributeDetailsSection_errorTitle, e
                                    .getLocalizedMessage());
                }
            }
        });
        return text;
    }

    private static Control createBooleanControl(final Composite parent,
            FormToolkit toolkit, boolean writable, Object value,
            final IWritableAttributeHandler handler) {
        boolean booleanValue = ((Boolean) value).booleanValue();
        if (!writable) {
            Text text = toolkit.createText(parent, Boolean
                    .toString(booleanValue), SWT.SINGLE);
            text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
            return text;
        }

        final Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        toolkit.paintBordersFor(combo);
        combo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        combo.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        combo.setItems(new String[] { Boolean.TRUE.toString(),
                Boolean.FALSE.toString() });
        if (booleanValue) {
            combo.select(0);
        } else {
            combo.select(1);
        }
        combo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                Boolean newValue = Boolean.valueOf(combo.getText());
                handler.write(newValue);
            }
        });
        return combo;
    }

    private static Control createArrayControl(final Composite parent,
            FormToolkit toolkit, Object arrayObj) {
        final Table table = toolkit.createTable(parent, SWT.BORDER
                | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        toolkit.paintBordersFor(table);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        setupArrayControl(table, arrayObj);
        return table;
    }

    private static Control createArrayControl(final Composite parent,
            Object arrayObj) {
        final Table table = new Table(parent, SWT.BORDER | SWT.READ_ONLY
                | SWT.H_SCROLL | SWT.V_SCROLL);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        setupArrayControl(table, arrayObj);
        return table;
    }

    private static void setupArrayControl(Table table, Object arrayObj) {
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(150);
        table.setLinesVisible(true);
        populateTableItems(table, arrayObj);
    }

    private static void populateTableItems(Table table, Object arrayObj) {
        int length = Array.getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(arrayObj, i);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.toString(element, false));
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static Control createCollectionControl(final Composite parent,
            FormToolkit toolkit, Collection collection) {
        final Table table = toolkit.createTable(parent, SWT.BORDER
                | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        toolkit.paintBordersFor(table);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(150);
        table.setLinesVisible(true);
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            Object element = (Object) iter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.toString(element, false));
        }
        return table;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static Control createMapControl(final Composite parent,
            FormToolkit toolkit, Map map) {
        final Table table = toolkit.createTable(parent, SWT.BORDER
                | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        toolkit.paintBordersFor(table);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        TableColumn keyColumn = new TableColumn(table, SWT.NONE);
        keyColumn.setText(Messages.key);
        keyColumn.setWidth(150);
        TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setText(Messages.value);
        valueColumn.setWidth(150);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, StringUtils.toString(entry.getKey(), false));
            item.setText(1, StringUtils.toString(entry.getValue(), false));
        }
        return table;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static Control createCompositeDataControl(final Composite parent,
            FormToolkit toolkit, CompositeData data) {
        final Table table = toolkit.createTable(parent, SWT.BORDER
                | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        toolkit.paintBordersFor(table);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn keyColumn = new TableColumn(table, SWT.NONE);
        keyColumn.setText(Messages.key);
        keyColumn.setWidth(150);
        TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setText(Messages.value);
        valueColumn.setWidth(150);
        Iterator iter = data.getCompositeType().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, key);
            item.setText(1, StringUtils.toString(data.get(key), false));
        }
        return table;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static Control createTabularDataControl(final Composite parent,
            FormToolkit toolkit, TabularData data) {
        final Table table = toolkit.createTable(parent, SWT.RESIZE | SWT.SINGLE
                | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        toolkit.paintBordersFor(parent);
        table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        Set keySet = data.getTabularType().getRowType().keySet();
        Iterator keyIter = keySet.iterator();
        while (keyIter.hasNext()) {
            String key = (String) keyIter.next();
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText(key);
            column.setWidth(150);
            column.setMoveable(true);
            column.setResizable(true);
        }
        Iterator valueIter = data.values().iterator();
        while (valueIter.hasNext()) {
            CompositeData rowData = (CompositeData) valueIter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            keyIter = keySet.iterator();
            int i = 0;
            while (keyIter.hasNext()) {
                String key = (String) keyIter.next();
                item.setText(i, StringUtils.toString(rowData.get(key), false));
                i++;
            }
        }
        return table;
    }

}
