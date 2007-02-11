/**
 * Eclipse JMX Console
 * Copyright (C) 2006 Jeff Mesnil
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
package net.jmesnil.jmx.ui.internal.views.opinvocation;

import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.controls.AttributeControlFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class InvocationResultDialog extends Dialog {

    private Object result;

    public InvocationResultDialog(Shell parentShell, Object result) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
        this.result = result;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.InvocationResultDialog_title);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new TableWrapLayout());
        Control resultControl = AttributeControlFactory.createControl(
                composite, result);
        if (resultControl == null) {
            Label label = new Label(composite, SWT.NONE);
            label.setText("" + result); //$NON-NLS-1$
        }
        return composite;
    }

    public static void open(Shell shell, Object result) {
        Dialog dialog = new InvocationResultDialog(shell, result);
        dialog.open();
    }
}