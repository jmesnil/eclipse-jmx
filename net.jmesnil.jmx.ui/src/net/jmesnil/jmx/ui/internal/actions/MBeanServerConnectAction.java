/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import javax.management.MBeanServerConnection;

import net.jmesnil.jmx.resources.MBeanServerConnectionDescriptor;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;

public class MBeanServerConnectAction extends Action {

    private final MBeanExplorer view;

    public MBeanServerConnectAction(MBeanExplorer view) {
        super(Messages.MBeanServerConnectAction_text, AS_PUSH_BUTTON);
        this.view = view;
        JMXImages.setLocalImageDescriptors(this, "attachAgent.gif"); //$NON-NLS-1$
    }

    @Override
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
            MBeanServerConnectionDescriptor connectionDescriptor  = new MBeanServerConnectionDescriptor(url, url, userName, password);  
            MBeanServerConnection mbsc = JMXUIActivator.getDefault().getConnectionFactory().createMBeanServerConnection(connectionDescriptor);
            view.setMBeanServerConnection(mbsc);
            JMXUIActivator.getDefault().setCurrentConnection(mbsc);
        } catch (Exception e) {
            String message = Messages.MBeanServerConnectAction_connectionFailure;
            ErrorDialog.openError(view.getSite().getShell(),
                    Messages.MBeanServerConnectAction_error, message,
                    new Status(IStatus.ERROR, JMXUIActivator.PLUGIN_ID,
                            IStatus.OK, message, e));
        }
    }
}
