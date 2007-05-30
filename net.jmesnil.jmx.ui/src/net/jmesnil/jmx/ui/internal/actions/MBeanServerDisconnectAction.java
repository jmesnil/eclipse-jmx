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

package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditor;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;

public class MBeanServerDisconnectAction extends Action {

    private MBeanExplorer view;

    public MBeanServerDisconnectAction(MBeanExplorer view) {
        super(Messages.MBeanServerDisconnectAction_text, AS_PUSH_BUTTON);
        this.view = view;
        JMXImages.setLocalImageDescriptors(this, "detachAgent.gif"); //$NON-NLS-1$
    }

    public void run() {
        IEditorReference[] references = JMXUIActivator.getActivePage()
                .findEditors(null, MBeanEditor.ID, IWorkbenchPage.MATCH_ID);
        boolean close = true;
        if (references.length > 0) {
            close = MessageDialog.openConfirm(JMXUIActivator
                    .getActiveWorkbenchShell(),
                    Messages.MBeanServerDisconnectAction_dialogTitle,
                    Messages.MBeanServerDisconnectAction_dialogText);
        }
        if (close) {
            JMXUIActivator.getActivePage().closeEditors(references, false);
            view.setMBeanServerConnection(null);
        }
    }
}