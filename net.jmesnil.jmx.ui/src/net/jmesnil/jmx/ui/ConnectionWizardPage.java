/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.ui;

import net.jmesnil.jmx.resources.IConnectionWrapper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * A page for a connection type's wizard creation
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public interface ConnectionWizardPage extends IWizardPage {
	/*
	 * Called during the wizard.performFinish() command
	 * to retrieve the completed / created connection
	 * object so it can be added properly.
	 */
	public IConnectionWrapper getConnection() throws CoreException;
}
