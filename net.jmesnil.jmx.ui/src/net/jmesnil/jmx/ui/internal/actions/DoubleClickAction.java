/*******************************************************************************
 * Copyright (c) 2008 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

/**
 * The double click action
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class DoubleClickAction extends Action implements
		ISelectionChangedListener {
	private ISelection selection;
	public void selectionChanged(SelectionChangedEvent event) {
		this.selection = event.getSelection();
	}

	public void run() {
		StructuredSelection structured = (StructuredSelection) selection;
		Object element = structured.getFirstElement();
		IEditorInput editorInput = EditorUtils.getEditorInput(element);
		if (editorInput != null) {
			IEditorPart editor = EditorUtils
					.openMBeanEditor(editorInput);
			if (editor != null) {
				EditorUtils.revealInEditor(editor, element);
			}
		}
	}
}
