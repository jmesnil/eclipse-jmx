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
 * 
 * Contributors:
 *      Benjamin Walstrum (issue #24)
 */
package net.jmesnil.jmx.ui.internal.controls;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.extensions.IAttributeControlFactory;
import net.jmesnil.jmx.ui.extensions.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

public class TextControlFactory implements IAttributeControlFactory {

	public Control createControl(final Composite parent, final FormToolkit toolkit,
			final boolean writable, final String type, final Object value, 
			final IWritableAttributeHandler handler) {
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
            final Text text = createTextControl(parent, toolkit, style);
            text.setText(attrValue);
            text.setEditable(false);
            text.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLACK));
            return text;
        } else {
            // interpose a composite to contain both
            // the text control and an "update" button
            Composite composite = toolkit.createComposite(parent);
            composite
                    .setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            GridLayout layout = new GridLayout(2, false);
            composite.setLayout(layout);

            final Text text = createTextControl(composite, toolkit, style);
            text.setText(attrValue);
            text.setEditable(true);
            text.setForeground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_BLUE));
            if (handler != null) {
                Button updateButton = toolkit.createButton(composite,
                    Messages.AttributeControlFactory_updateButtonTitle, SWT.PUSH);
                updateButton.setLayoutData(new GridData(SWT.END, SWT.TOP,
                        false, false));
                updateButton.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent event) {
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

                    public void widgetSelected(SelectionEvent event) {
                        widgetDefaultSelected(event);
                    }

                });
            }
            return text;
        }
	}

    private static Text createTextControl(final Composite parent,
            FormToolkit toolkit, int style) {
        final Text text;
        if (toolkit != null) {
            text = toolkit.createText(parent, "", style); //$NON-NLS-1$
        } else {
            text = new Text(parent, style); //$NON-NLS-1$    
        }
        return text;
    }

}
