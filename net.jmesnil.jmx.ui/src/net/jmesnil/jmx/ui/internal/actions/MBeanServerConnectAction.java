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

import java.io.IOException;

import net.jmesnil.jmx.core.JMXCoreActivator;
import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.views.MBeanView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class MBeanServerConnectAction implements IViewActionDelegate {

    private MBeanView view;

    public void init(IViewPart view) {
        this.view = (MBeanView) view;
    }

    public void run(IAction action) {
        MBeanServerConnectDialog dialog = new MBeanServerConnectDialog(view
                .getViewSite().getShell());
        if (dialog.open() != Window.OK) {
            return;
        }
        try {
            String host = dialog.getHost();
            int port = dialog.getPort();
            MBeanServerConnectionWrapper connection = JMXCoreActivator
                    .getDefault().connect(
                            "service:jmx:rmi:///jndi/rmi://" + host + ":" //$NON-NLS-1$ //$NON-NLS-2$
                                    + port + "/jmxrmi"); //$NON-NLS-1$
            view.setMBeanServerConnection(connection);
        } catch (IOException e) {
            MessageDialog.openError(view.getSite().getShell(),
                    Messages.MBeanServerConnectAction_error, e.getMessage());
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}
