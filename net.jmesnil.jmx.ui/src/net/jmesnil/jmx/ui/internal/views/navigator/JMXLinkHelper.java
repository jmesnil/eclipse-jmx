/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.core.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.ui.internal.EditorUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

/**
 *
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
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
