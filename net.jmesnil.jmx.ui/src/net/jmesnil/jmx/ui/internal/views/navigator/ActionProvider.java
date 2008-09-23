/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.ui.internal.views.navigator;

import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.ui.internal.actions.DeleteConnectionAction;
import net.jmesnil.jmx.ui.internal.actions.DoubleClickAction;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerConnectAction;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerDisconnectAction;
import net.jmesnil.jmx.ui.internal.actions.NewConnectionAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

/**
 *
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class ActionProvider extends CommonActionProvider {
	private DoubleClickAction doubleClickAction;
	private NewConnectionAction newConnectionAction;
	public ActionProvider() {
		super();
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        doubleClickAction = new DoubleClickAction();
        newConnectionAction = new NewConnectionAction();
        aSite.getStructuredViewer().addSelectionChangedListener(doubleClickAction);
    }

    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
        actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
              doubleClickAction);
    }

    public void fillContextMenu(IMenuManager menu) {
    	IConnectionWrapper connection = getWrapperFromSelection();
    	if( connection != null ) {
	    	if( !connection.isConnected() && connection.canControl())
	    		menu.add(new MBeanServerConnectAction(connection));
	    	else if( connection.canControl())
	    		menu.add(new MBeanServerDisconnectAction(connection));

	    	menu.add(new DeleteConnectionAction(connection));
    	}

    	// Finish up
    	menu.add(new Separator());
    	menu.add(newConnectionAction);
    }

    protected IConnectionWrapper getWrapperFromSelection() {
    	if( getContext() != null && getContext().getSelection() != null ) {
    		ISelection sel = getContext().getSelection();
    		if( sel instanceof IStructuredSelection ) {
    			Object first = ((IStructuredSelection)sel).getFirstElement();
    			if( first instanceof IConnectionWrapper ) {
    				return ((IConnectionWrapper)first);
    			}
    		}
    	}
    	return null;
    }
}
