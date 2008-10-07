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

import net.jmesnil.jmx.core.DeleteConnectionJob;
import net.jmesnil.jmx.core.DisconnectJob;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.ui.Messages;

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
		setText(Messages.DeleteConnection);
	}

	public void run() {
		if( connection != null ) {
			final IConnectionWrapper[] wrapper = connection;
			DisconnectJob dj = new DisconnectJob(wrapper);
			DeleteConnectionJob deleteJob = new DeleteConnectionJob(wrapper);
			dj.setNextJob(deleteJob);
			dj.schedule();
		}
	}
}
