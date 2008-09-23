/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.actions;

import net.jmesnil.jmx.ui.JMXMessages;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.wizards.NewConnectionWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Create a new connection
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class NewConnectionAction extends Action {
	public NewConnectionAction() {
		super(JMXMessages.NewConnectionAction);
        JMXImages.setLocalImageDescriptors(this, "attachAgent.gif");  //$NON-NLS-1$
	}

	public void run() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				IWizard wizard = new NewConnectionWizard();
				WizardDialog d = new WizardDialog(new Shell(), wizard);
				d.open();
			}
		} );
	}
}
