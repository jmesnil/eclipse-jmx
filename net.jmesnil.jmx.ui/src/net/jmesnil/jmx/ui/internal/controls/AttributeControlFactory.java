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

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
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
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributeControlFactory {

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static Control createControl(final Composite parent, final Object value) {
        return createControl(parent, null, false, value.getClass().getSimpleName(), value, null);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static Control createControl(final Composite parent, FormToolkit toolkit,
            final boolean writable, final String type,
            final Object value, final IWritableAttributeHandler handler) {
        if (value != null && value instanceof Boolean) {
            return createBooleanControl(parent, toolkit, writable, value,
                    handler);
        }
        if (value != null && value.getClass().isArray()) {
            final Table table = createTable(parent, toolkit, false, true);
            fillArray(table, value);
            return table;
        }
        if (value != null && value instanceof CompositeData) {
            final Table table = createTable(parent, toolkit, true, true);
            fillCompositeData(table, (CompositeData) value);
            return table;
        }
        if (value != null && value instanceof TabularData) {
            final Table table = createTable(parent, toolkit, true, true);
            fillTabularData(table, (TabularData) value);
            return table;
        }
        if (value != null && value instanceof Collection) {
            final Table table = createTable(parent, toolkit, false, true);
            fillCollection(table, (Collection) value);
            return table;
        }
        if (value != null && value instanceof Map) {
            final Table table = createTable(parent, toolkit, true, true);
            fillMap(table, (Map) value);
            return table;
        }
        return createText(parent, toolkit, writable, type, value, handler);
    }

    private static Control createText(final Composite parent,
            FormToolkit toolkit, final boolean writable, final String type,
            final Object value, final IWritableAttributeHandler handler) {

        String attrValue = ""; //$NON-NLS-1$
        try {
            attrValue = StringUtils.toString(value, true);
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR,
                    Messages.MBeanAttributeValue_Warning, e);
            attrValue = Messages.unavailable;
        }

        int style = SWT.BORDER;
        // fixed issue #12
        if (value instanceof Number || value instanceof Character) {
            style |= SWT.SINGLE;
        } else {
            style |= SWT.MULTI | SWT.WRAP;
        }

        if (!writable) {
            final Text text = createText(parent, toolkit, style);
            text.setText(attrValue);
            text.setEditable(false);
            text.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLACK));
            return text;
        } else {
            Composite comp = toolkit.createComposite(parent);
            comp.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB,
                    TableWrapData.FILL_GRAB));
            TableWrapLayout twlayout = new TableWrapLayout();
            twlayout.numColumns = 2;
            twlayout.makeColumnsEqualWidth = false;
            comp.setLayout(twlayout);

            final Text text = createText(comp, toolkit, style);
            text.setText(attrValue);
            text.setEditable(true);
            text.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLUE));
            if (handler != null) {
                Button updateButton = toolkit.createButton(comp,
                        Messages.AttributeControlFactory_updateButtonTitle,
                        SWT.PUSH);
                updateButton.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent event) {
                        widgetSelected(event);
                    }

                    public void widgetSelected(SelectionEvent event) {
                        try {
                            Object newValue = MBeanUtils.getValue(text
                                    .getText(), type);
                            handler.write(newValue);
                            text.setText(newValue.toString());
                        } catch (Throwable t) {
                            IStatus errorStatus = new Status(IStatus.ERROR,
                                    JMXUIActivator.PLUGIN_ID, IStatus.OK, t
                                            .getMessage(), t);
                            ErrorDialog
                                    .openError(
                                            parent.getShell(),
                                            Messages.AttributeDetailsSection_errorTitle,
                                            t.getMessage(), errorStatus);
                        }
                    }
                });
            }
            return text;
        }
    }

    private static Text createText(final Composite parent, FormToolkit toolkit,
            int style) {
        if (toolkit != null) {
            return toolkit.createText(parent, "", style); //$NON-NLS-1$
        } else {
            return new Text(parent, style); //$NON-NLS-1$    
        }
    }

    private static Control createBooleanControl(final Composite parent,
            FormToolkit toolkit, boolean writable, Object value,
            final IWritableAttributeHandler handler) {
        boolean booleanValue = ((Boolean) value).booleanValue();
        if (!writable) {
            if (toolkit != null) {
                return toolkit.createText(parent, Boolean
                        .toString(booleanValue), SWT.SINGLE);
            } else {
                Text text = new Text(parent, SWT.SINGLE);
                text.setText(Boolean.toString(booleanValue));
                return text;
            }
        }

        final Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        if (toolkit != null) {
            combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
            toolkit.paintBordersFor(combo);
        }
        combo.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        combo.setItems(new String[] { Boolean.TRUE.toString(),
                Boolean.FALSE.toString() });
        if (booleanValue) {
            combo.select(0);
        } else {
            combo.select(1);
        }
        if (handler != null) {
            combo.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    Boolean newValue = Boolean.valueOf(combo.getText());
                    handler.write(newValue);
                }
            });
        }
        return combo;
    }

    private static Table createTable(Composite parent, FormToolkit toolkit, boolean visibleHeader, boolean visibleLines) {
        int style = SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL;
        Table table = null;
        if (toolkit != null) {
            table = toolkit.createTable(parent, style);
            toolkit.paintBordersFor(table);
        } else {
            table = new Table(parent, style);
        }
        table.setHeaderVisible(visibleHeader);
        table.setLinesVisible(visibleLines);
        return table;
    }

    private static void fillArray(Table table, Object arrayObj) {
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(150);
        fillArrayItems(table, arrayObj);
    }

    private static void fillArrayItems(Table table, Object arrayObj) {
        int length = Array.getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(arrayObj, i);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.toString(element, false));
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static void fillCollection(final Table table, Collection collection) {
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(150);
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            Object element = (Object) iter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.toString(element, false));
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static void fillMap(final Table table, Map map) {
        TableColumn keyColumn = new TableColumn(table, SWT.NONE);
        keyColumn.setText(Messages.key);
        keyColumn.setWidth(150);
        TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setText(Messages.value);
        valueColumn.setWidth(150);
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, StringUtils.toString(entry.getKey(), false));
            item.setText(1, StringUtils.toString(entry.getValue(), false));
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static void fillCompositeData(final Table table,
            CompositeData data) {
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
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private static void fillTabularData(final Table table,
            TabularData data) {
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
    }

}
