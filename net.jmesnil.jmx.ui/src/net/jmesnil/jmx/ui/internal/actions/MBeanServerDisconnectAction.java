/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import java.io.IOException;

import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.ui.JMXMessages;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

public class MBeanServerDisconnectAction extends Action {
	private IConnectionWrapper connection;
    public MBeanServerDisconnectAction(IConnectionWrapper wrapper) {
        super(Messages.MBeanServerDisconnectAction_text, AS_PUSH_BUTTON);
        JMXImages.setLocalImageDescriptors(this, "detachAgent.gif"); //$NON-NLS-1$
        this.connection = wrapper;
    }

    public void run() {
		if( connection != null ) {
			final IConnectionWrapper wrapper = connection;
			new Job(JMXMessages.CloseJMXConnectionJob) {
				protected IStatus run(IProgressMonitor monitor) {
					try {
						wrapper.disconnect();
					} catch( IOException ioe ) {
						return new Status(IStatus.ERROR, JMXUIActivator.PLUGIN_ID, JMXMessages.CloseJMXConnectionError, ioe);
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
    	// TODO close editors on disconnect
    }
}