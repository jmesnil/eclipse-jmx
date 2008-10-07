/*******************************************************************************
 * Copyright (c) 2008 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    "Rob Stryker" <rob.stryker@redhat.com> - Initial implementation
 *******************************************************************************/
package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.core.IConnectionWrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The action to delete a connection
 */
public class DeleteConnectionAction extends Action {
	private IConnectionWrapper[] connection;
	public DeleteConnectionAction(IConnectionWrapper[] wrapper) {
		connection = wrapper;
		boolean enabled = true;
		for( int i = 0; i < connection.length; i++ ) 
			if( !connection[i].getProvider().canDelete(connection[i]) )
					enabled = false;
		String key = enabled ? ISharedImages.IMG_TOOL_DELETE : ISharedImages.IMG_TOOL_DELETE_DISABLED;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(key));
		setEnabled(enabled);
		setText("Delete Connection");
	}

	public void run() {
		if( connection != null ) {
			final IConnectionWrapper[] wrapper = connection;
			new Job("Delete JMX Connection Job") {
				protected IStatus run(IProgressMonitor monitor) {
					for( int i = 0; i < connection.length; i++ )
						wrapper[i].getProvider().removeConnection(wrapper[i]);
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}
}
