/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    "Rob Stryker" <rob.stryker@redhat.com> - Initial implementation
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.core.ConnectJob;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.Messages;

import org.eclipse.jface.action.Action;

/**
 * The connect action
 */
public class MBeanServerConnectAction extends Action {
	private IConnectionWrapper[] connection;
    public MBeanServerConnectAction(IConnectionWrapper[] wrapper) {
        super(Messages.MBeanServerConnectAction_text, AS_PUSH_BUTTON);
        JMXImages.setLocalImageDescriptors(this, "attachAgent.gif"); //$NON-NLS-1$
        this.connection = wrapper;
    }

	public void run() {
		if( connection != null ) {
			new ConnectJob(connection).schedule();
		}
    }
}
