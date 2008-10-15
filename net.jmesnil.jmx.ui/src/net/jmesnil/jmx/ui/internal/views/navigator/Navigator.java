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

import net.jmesnil.jmx.ui.internal.actions.NewConnectionAction;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * The view itself
 */
public class Navigator extends CommonNavigator {
	public static final String VIEW_ID = "net.jmesnil.jmx.ui.internal.views.navigator.MBeanExplorer"; //$NON-NLS-1$
	private QueryContribution queryContribution;
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
		queryContribution = new QueryContribution(this);
	    getViewSite().getActionBars().getToolBarManager().add(queryContribution);
	    getViewSite().getActionBars().getToolBarManager().add(new NewConnectionAction());
	    getViewSite().getActionBars().getToolBarManager().add(new Separator());
	    getViewSite().getActionBars().updateActionBars();
	}
}
