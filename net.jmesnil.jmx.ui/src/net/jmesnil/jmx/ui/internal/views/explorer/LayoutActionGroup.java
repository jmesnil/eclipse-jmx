/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.views.explorer;

import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionGroup;

public class LayoutActionGroup extends ActionGroup {

    private boolean hasContributedToViewMenu = false;

    private IAction hierarchicalLayoutAction = null;
    private IAction flatLayoutAction = null;
    private IAction[] actions;

    private MBeanExplorer view;

    private class LayoutAction extends Action implements IAction {

        private final boolean flat;

        public LayoutAction(boolean flat) {
            super("", AS_RADIO_BUTTON); //$NON-NLS-1$
            this.flat = flat;
        }

        public void run() {
            if (view.isCurrentLayoutFlat() != flat) {
                view.toggleLayout();
            }
        }
    }

    public LayoutActionGroup(MBeanExplorer view) {
        this.view = view;
    }

    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
        if (!hasContributedToViewMenu) {
            synchronized (this) {
                if (!hasContributedToViewMenu) {
                    hasContributedToViewMenu = true;
                    contributeToViewMenu(actionBars.getMenuManager());
                }
            }
        }
    }

    private void contributeToViewMenu(IMenuManager viewMenu) {
        viewMenu.add(new Separator());

        // Create layout sub menu

        IMenuManager layoutSubMenu = new MenuManager(
                Messages.LayoutActionGroup_menu);
        final String layoutGroupName = "layout"; //$NON-NLS-1$
        Separator marker = new Separator(layoutGroupName);

        viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        viewMenu.add(marker);
        viewMenu.appendToGroup(layoutGroupName, layoutSubMenu);
        viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS
                + "-end"));//$NON-NLS-1$        

        layoutSubMenu.add(hierarchicalLayoutAction);
        layoutSubMenu.add(flatLayoutAction);
    }

    private IAction[] createActions() {

        flatLayoutAction = new LayoutAction(true);
        flatLayoutAction.setText(Messages.LayoutActionGroup_flat);
        JMXImages.setLocalImageDescriptors(flatLayoutAction, "flatLayout.gif"); //$NON-NLS-1$

        hierarchicalLayoutAction = new LayoutAction(false);
        hierarchicalLayoutAction
                .setText(Messages.LayoutActionGroup_hierarchical);
        JMXImages.setLocalImageDescriptors(hierarchicalLayoutAction,
                "hierarchicalLayout.gif"); //$NON-NLS-1$

        return new IAction[] { flatLayoutAction, hierarchicalLayoutAction };
    }

    public void setFlatLayout(boolean flatLayout) {
        if (actions == null) {
            actions = createActions();
            flatLayoutAction.setChecked(false);
            hierarchicalLayoutAction.setChecked(true);
        }
        hierarchicalLayoutAction.setChecked(!flatLayout);
        flatLayoutAction.setChecked(flatLayout);
    }
}
