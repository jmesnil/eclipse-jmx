package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.core.IConnectionWrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class DeleteConnectionAction extends Action {
	private IConnectionWrapper connection;
	public DeleteConnectionAction(IConnectionWrapper wrapper) {
		connection = wrapper;
		boolean enabled = connection != null && connection.getProvider().canDelete(connection);
		String key = enabled ? ISharedImages.IMG_TOOL_DELETE : ISharedImages.IMG_TOOL_DELETE_DISABLED;
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(key));
		setEnabled(enabled);
		setText("Delete Connection");
	}

	public void run() {
		if( connection != null ) {
			final IConnectionWrapper wrapper = connection;
			new Job("Delete JMX Connection Job") {
				protected IStatus run(IProgressMonitor monitor) {
					wrapper.getProvider().removeConnection(wrapper);
					return Status.OK_STATUS;
				}
			}.schedule();
		}

	}
}
