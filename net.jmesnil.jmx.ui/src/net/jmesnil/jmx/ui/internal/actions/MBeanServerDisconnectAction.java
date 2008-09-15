/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditor;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;

public class MBeanServerDisconnectAction extends Action {

    private MBeanExplorer view;

    public MBeanServerDisconnectAction(MBeanExplorer view) {
        super(Messages.MBeanServerDisconnectAction_text, AS_PUSH_BUTTON);
        this.view = view;
        JMXImages.setLocalImageDescriptors(this, "detachAgent.gif"); //$NON-NLS-1$
    }

    public void run() {
        IEditorReference[] references = JMXUIActivator.getActivePage()
                .findEditors(null, MBeanEditor.ID, IWorkbenchPage.MATCH_ID);
        boolean close = true;
        if (references.length > 0) {
            close = MessageDialog.openConfirm(JMXUIActivator
                    .getActiveWorkbenchShell(),
                    Messages.MBeanServerDisconnectAction_dialogTitle,
                    Messages.MBeanServerDisconnectAction_dialogText);
        }
        if (close) {
            JMXUIActivator.getActivePage().closeEditors(references, false);
            view.setMBeanServerConnection(null);
        }
    }
}