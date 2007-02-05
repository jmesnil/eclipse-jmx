/**
 * Eclipse JMX Console
 * Copyright (C) 200è Jeff Mesnil
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

import javax.management.MBeanAttributeInfo;

import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;
import net.jmesnil.jmx.ui.internal.UpdatableAttributeHandler;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class AttributeControlFactory {

    public static Control createControl(final Composite parent,
            FormToolkit toolkit, final MBeanAttributeInfo attrInfo,
            Object value, final UpdatableAttributeHandler handler) {
        if (value.getClass().equals(Boolean.class)) {
            return createCombo(parent, toolkit, attrInfo, value, handler);
        } else {
            return createText(parent, toolkit, attrInfo, value, handler);
        }
    }

    private static Control createText(final Composite parent, FormToolkit toolkit,
            final MBeanAttributeInfo attrInfo, Object value,
            final UpdatableAttributeHandler handler) {
        String attrValue = ""; //$NON-NLS-1$
        try {
            attrValue = StringUtils.toString(value, true);
        } catch (Exception e) {
            attrValue = Messages.unavailable;
        }

        final Text attrValueText = toolkit.createText(parent,
                "", SWT.SINGLE | SWT.WRAP); //$NON-NLS-1$
        attrValueText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        if (attrValue.equals(Messages.unavailable)) {
            attrValueText.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_RED));
        } else {
            if (attrInfo.isWritable()) {
                attrValueText.setEditable(true);
                attrValueText.setForeground(parent.getDisplay().getSystemColor(
                        SWT.COLOR_BLUE));
                attrValueText.addListener(SWT.DefaultSelection, new Listener() {
                    public void handleEvent(Event event) {
                        try {
                            Object newValue = MBeanUtils.getValue(attrValueText
                                    .getText(), attrInfo.getType());
                            handler.update(newValue);
                        } catch (Exception e) {
                            MessageDialog
                                    .openError(
                                            parent.getShell(),
                                            Messages.AttributeDetailsSection_errorTitle,
                                            e.getLocalizedMessage());
                        }
                    }
                });
            } else {
                attrValueText.setEditable(false);
                attrValueText.setForeground(parent.getDisplay().getSystemColor(
                        SWT.COLOR_BLACK));
            }
        }
        attrValueText.setText(attrValue);
        return attrValueText;
    }

    private static Control createCombo(final Composite parent,
            FormToolkit toolkit, MBeanAttributeInfo attrInfo, Object value,
            final UpdatableAttributeHandler handler) {
        final Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        toolkit.paintBordersFor(combo);
        combo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        if (!attrInfo.isWritable()) {
            combo.add(value.toString());
            combo.select(0);
        } else {
            combo.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLUE));
            combo.setItems(new String[] { Boolean.TRUE.toString(),
                    Boolean.FALSE.toString() });
            if (((Boolean) value).booleanValue()) {
                combo.select(0);
            } else {
                combo.select(1);
            }
            combo.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    Boolean newValue = Boolean.valueOf(combo.getText());
                    handler.update(newValue);
                }
            });
        }
        return combo;
    }
}
