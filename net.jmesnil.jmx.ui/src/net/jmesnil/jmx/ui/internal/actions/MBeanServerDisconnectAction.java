/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.core.DisconnectJob;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.Messages;
import net.jmesnil.jmx.ui.internal.JMXImages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Disconnect from a server
 */
public class MBeanServerDisconnectAction extends Action {
	private IConnectionWrapper[] connection;
    public MBeanServerDisconnectAction(IConnectionWrapper[] wrapper) {
        super(Messages.MBeanServerDisconnectAction_text, AS_PUSH_BUTTON);
        JMXImages.setLocalImageDescriptors(this, "detachAgent.gif"); //$NON-NLS-1$
        this.connection = wrapper;
    }

    public void run() {
		if( connection != null ) {
			if( showDialog(connection))
				new DisconnectJob(connection).schedule();
		}
    }
    
    protected boolean showDialog(IConnectionWrapper[] wrappers) {
        return MessageDialog.openConfirm(JMXUIActivator
                .getActiveWorkbenchShell(),
                Messages.MBeanServerDisconnectAction_dialogTitle,
                Messages.MBeanServerDisconnectAction_dialogText);
    }
}