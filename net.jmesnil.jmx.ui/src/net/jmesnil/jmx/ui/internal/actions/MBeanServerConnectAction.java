/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import java.io.IOException;

import net.jmesnil.jmx.resources.IConnectionWrapper;
import net.jmesnil.jmx.ui.JMXMessages;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

/**
 * The connect action
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class MBeanServerConnectAction extends Action {
	private IConnectionWrapper connection;
    public MBeanServerConnectAction(IConnectionWrapper wrapper) {
        super(Messages.MBeanServerConnectAction_text, AS_PUSH_BUTTON);
        JMXImages.setLocalImageDescriptors(this, "attachAgent.gif"); //$NON-NLS-1$
        this.connection = wrapper;
    }

	public void run() {
		if( connection != null ) {
			final IConnectionWrapper wrapper = connection;
			new Job(JMXMessages.OpenJMXConnectionJob) {
				protected IStatus run(IProgressMonitor monitor) {
					try {
						wrapper.connect();
					} catch( IOException ioe ) {
						// TODO EXTERNALIZE STRING
						return new Status(IStatus.ERROR, JMXUIActivator.PLUGIN_ID, JMXMessages.OpenJMXConnectionError, ioe);
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
    }
}
