/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
 * Contact: http://www.jmesnil.net
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
package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.EditorUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.dialogs.OpenMBeanSelectionDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenMBeanAction extends Action implements
        IWorkbenchWindowActionDelegate {

    public OpenMBeanAction() {
        super();
        setText(Messages.OpenMBeanAction_text);
        setDescription(Messages.OpenMBeanAction_description);
        setToolTipText(Messages.OpenMBeanAction_tooltip);
    }

    @Override
    public void run() {
        Shell parent = JMXUIActivator.getActiveWorkbenchShell();
        OpenMBeanSelectionDialog dialog = new OpenMBeanSelectionDialog(parent);
        dialog.setTitle(Messages.OpenMBeanAction_dialogTitle);
        dialog.setMessage(Messages.OpenMBeanAction_dialogDescription);
        int result = dialog.open();
        if (result != IDialogConstants.OK_ID)
            return;
        Object object = dialog.getFirstResult();
        IEditorInput input = EditorUtils.getEditorInput(object);
        if (input != null) {
            EditorUtils.openMBeanEditor(input);
        }
    }

    public void run(IAction action) {
        run();
    }

    public void dispose() {
        // do nothing
    }

    public void init(IWorkbenchWindow window) {
        // do nothing
    }

    public void selectionChanged(IAction action, ISelection selection) {
        // do nothing
    }

}
