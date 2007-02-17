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

import net.jmesnil.jmx.core.JMXCoreActivator;
import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;

public class MBeanServerConnectAction extends Action {

    private MBeanExplorer view;

    public MBeanServerConnectAction(MBeanExplorer view) {
        super(Messages.MBeanServerConnectAction_text, AS_PUSH_BUTTON);
        this.view = view;
        JMXImages.setLocalImageDescriptors(this, "attachAgent.gif"); //$NON-NLS-1$
    }

    public void run() {
        MBeanServerConnectDialog dialog = new MBeanServerConnectDialog(view
                .getViewSite().getShell());
        if (dialog.open() != Window.OK) {
            return;
        }
        try {
            String url = dialog.getURL();
            String userName = dialog.getUserName();
            String password = dialog.getPassword();
            MBeanServerConnectionWrapper connection = JMXCoreActivator
                    .getDefault().connect(url, userName, password);
            view.setMBeanServerConnection(connection);
        } catch (Exception e) {
            String message = Messages.MBeanServerConnectAction_connectionFailure;
            ErrorDialog.openError(view.getSite().getShell(),
                    Messages.MBeanServerConnectAction_error, message,
                    new Status(IStatus.ERROR, JMXCoreActivator.PLUGIN_ID,
                            IStatus.OK, message, e));
        }
    }
}
