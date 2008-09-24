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
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.Messages;

import org.eclipse.jface.action.Action;

/**
 * Disconnect from a server
 */
public class MBeanServerDisconnectAction extends Action {
	private IConnectionWrapper connection;
    public MBeanServerDisconnectAction(IConnectionWrapper wrapper) {
        super(Messages.MBeanServerDisconnectAction_text, AS_PUSH_BUTTON);
        JMXImages.setLocalImageDescriptors(this, "detachAgent.gif"); //$NON-NLS-1$
        this.connection = wrapper;
    }

    public void run() {
		if( connection != null ) {
			new DisconnectJob(connection).schedule();
		}
    	// TODO close editors on disconnect
    }
}