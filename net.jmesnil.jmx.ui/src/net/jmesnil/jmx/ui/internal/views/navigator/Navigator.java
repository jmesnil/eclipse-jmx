/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.ui.internal.actions.NewConnectionAction;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 *
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class Navigator extends CommonNavigator {
	public static final String VIEW_ID = "net.jmesnil.jmx.ui.internal.views.navigator.MBeanExplorer"; //$NON-NLS-1$
	public Navigator() {
		super();
	}
	protected IAdaptable getInitialInput() {
		return this;
	}
	public void createPartControl(Composite aParent) {
		fillActionBars();
		super.createPartControl(aParent);
	}

	public void fillActionBars() {
	    getViewSite().getActionBars().getToolBarManager().add(new NewConnectionAction());
	    getViewSite().getActionBars().updateActionBars();
	}

}
