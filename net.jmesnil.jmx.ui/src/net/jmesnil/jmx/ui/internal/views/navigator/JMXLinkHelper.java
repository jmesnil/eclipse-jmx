package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.core.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.ui.internal.EditorUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

public class JMXLinkHelper implements ILinkHelper {

	public void activateEditor(IWorkbenchPage page,
			IStructuredSelection selection) {
		Object obj = selection.getFirstElement();
		if (selection.size() == 1) {
			IEditorPart part = EditorUtils.isOpenInEditor(obj);
			if (part != null) {
				if (obj instanceof MBeanFeatureInfoWrapper) {
					EditorUtils.revealInEditor(part, obj);
				}
			}
		}
	}

	public IStructuredSelection findSelection(IEditorInput anInput) {
		return null;
	}

}
