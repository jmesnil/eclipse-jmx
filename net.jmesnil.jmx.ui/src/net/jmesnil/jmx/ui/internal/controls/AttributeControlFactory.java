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

import javax.management.MBeanAttributeInfo;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
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

    public static Control createControl(final Composite parent,
	    FormToolkit toolkit, final MBeanAttributeInfo attrInfo,
	    Object value, final IWritableAttributeHandler handler) {
	if (value != null && value.getClass().equals(Boolean.class)) {
	    return createBooleanControl(parent, toolkit, attrInfo, value,
		    handler);
	}
	if (value != null && value.getClass().isArray()) {
	    return createArrayControl(parent, toolkit, attrInfo, value);
	}
	return createText(parent, toolkit, attrInfo, value, handler);
    }

    private static Control createText(final Composite parent,
	    FormToolkit toolkit, final MBeanAttributeInfo attrInfo,
	    Object value, final IWritableAttributeHandler handler) {
	String attrValue = ""; //$NON-NLS-1$
	try {
	    attrValue = StringUtils.toString(value, true);
	} catch (Exception e) {
	    JMXUIActivator.log(IStatus.ERROR, NLS.bind(
		    Messages.MBeanAttributeValue_Warning, attrInfo.getName()),
		    e);
	    attrValue = Messages.unavailable;
	}

	final Text text = toolkit.createText(parent, attrValue, SWT.SINGLE
		| SWT.WRAP);
	text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

	if (attrValue.equals(Messages.unavailable)) {
	    text.setForeground(parent.getDisplay()
		    .getSystemColor(SWT.COLOR_RED));
	    return text;
	}
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
	    FormToolkit toolkit, MBeanAttributeInfo attrInfo, Object value,
	    final IWritableAttributeHandler handler) {
	boolean booleanValue = ((Boolean) value).booleanValue();
	if (!attrInfo.isWritable()) {
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
	    FormToolkit toolkit, MBeanAttributeInfo attrInfo, Object arrayObj) {
	final Table table = toolkit.createTable(parent, SWT.BORDER
		| SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
	toolkit.paintBordersFor(table);
	table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
	TableColumn columnName = new TableColumn(table, SWT.NONE);
	columnName.setText(Messages.name);
	columnName.setWidth(150);
	table.setLinesVisible(true);
	populateTableItems(table, arrayObj);
	return table;
    }

    private static void populateTableItems(Table table, Object arrayObj) {
	int length = Array.getLength(arrayObj);
	for (int i = 0; i < length; i++) {
	    Object element = Array.get(arrayObj, i);
	    TableItem item = new TableItem(table, SWT.NONE);
	    item.setText(StringUtils.toString(element, false));
	}
    }
}
