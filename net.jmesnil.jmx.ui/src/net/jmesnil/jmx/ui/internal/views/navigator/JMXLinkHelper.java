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
package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.core.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.ui.internal.EditorUtils;
import net.jmesnil.jmx.ui.internal.editors.AttributesPage;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditor;
import net.jmesnil.jmx.ui.internal.editors.OperationsPage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

/**
 * The link helper to activate the editor
 */
public class JMXLinkHelper implements ILinkHelper {

	public void activateEditor(IWorkbenchPage page,
			IStructuredSelection selection) {
		Object obj = selection.getFirstElement();
		if (selection.size() == 1) {
			IEditorPart part = EditorUtils.isOpenInEditor(obj);
			if (part != null) {
				page.bringToTop(part);
				if (obj instanceof MBeanFeatureInfoWrapper) {
					EditorUtils.revealInEditor(part, obj);
				}
			}
		}
	}

	public IStructuredSelection findSelection(IEditorInput anInput) {
		IEditorPart part = EditorUtils.isOpenInEditor(anInput);
		if( part instanceof MBeanEditor ) {
			MBeanEditor editor = (MBeanEditor)part;
			Object page = editor.getSelectedPage();
			IStructuredSelection sel = null;
			if( page instanceof AttributesPage) {
				sel = ((AttributesPage)page).getSelection();
			} else if( page instanceof OperationsPage) {
				sel = ((OperationsPage)page).getSelection();
			}
			return sel;
		}
		
		return null;
	}

}
