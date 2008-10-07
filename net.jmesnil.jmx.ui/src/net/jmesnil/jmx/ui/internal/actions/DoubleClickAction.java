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

import net.jmesnil.jmx.ui.internal.EditorUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * The double click action
 */
public class DoubleClickAction extends Action implements
		ISelectionChangedListener {
	private ISelection selection;
	private CommonViewer viewer;
	public void selectionChanged(SelectionChangedEvent event) {
		this.selection = event.getSelection();
		viewer = (CommonViewer)event.getSource();
	}

	public void run() {
		StructuredSelection structured = (StructuredSelection) selection;
		Object element = structured.getFirstElement();
		viewer.expandToLevel(element, 1);
		IEditorInput editorInput = EditorUtils.getEditorInput(element);
		if (editorInput != null) {
			IEditorPart editor = EditorUtils
					.openMBeanEditor(editorInput);
			if (editor != null) {
				EditorUtils.revealInEditor(editor, element);
				editor.setFocus();
			}
		}
	}
}
