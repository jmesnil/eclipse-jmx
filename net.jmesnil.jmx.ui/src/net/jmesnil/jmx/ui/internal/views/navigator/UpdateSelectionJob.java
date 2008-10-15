package net.jmesnil.jmx.ui.internal.views.navigator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.NavigatorPlugin;
import org.eclipse.ui.internal.navigator.extensions.LinkHelperService;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.progress.UIJob;

public class UpdateSelectionJob extends UIJob {
	
	public static void launchJob(String viewId) {
        IWorkbench work = PlatformUI.getWorkbench();
        IWorkbenchWindow window = work.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IViewReference ref = window.getActivePage().findViewReference(viewId);
        if( ref != null ) {
            IWorkbenchPart part = ref.getPart(false);
            if ( part != null && page.isPartVisible(part)) {
            	if( part instanceof CommonNavigator)
            		new UpdateSelectionJob((CommonNavigator)part).schedule();
            }
        }
	}
	
	
	private CommonNavigator commonNavigator;
	private LinkHelperService linkService;
	public UpdateSelectionJob(CommonNavigator commonNavigator) {
		super("Updating Selection Job"); // TODO 
		this.commonNavigator = commonNavigator;
		linkService = new LinkHelperService((NavigatorContentService)commonNavigator.getCommonViewer().getNavigatorContentService());
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {

		if (!commonNavigator.getCommonViewer().getControl().isDisposed()) {
			SafeRunner.run(new ISafeRunnable() {

				public void run() throws Exception {
					IWorkbenchPage page = commonNavigator.getSite()
							.getPage();
					if (page != null) {
						IEditorPart editor = page.getActiveEditor();
						if (editor != null) {
							IEditorInput input = editor.getEditorInput();
							IStructuredSelection newSelection = linkService
									.getSelectionFor(input);
							if (!newSelection.isEmpty() && !newSelection.equals(commonNavigator.getCommonViewer().getSelection())) {
								commonNavigator.selectReveal(newSelection);
							}
						}
					}
				}

				public void handleException(Throwable e) {
					String msg = e.getMessage() != null ? e.getMessage()
							: e.toString();
					NavigatorPlugin.logError(0, msg, e);
				}
			});

		}

		return Status.OK_STATUS;
	}
}
