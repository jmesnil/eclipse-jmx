/**
 * Eclipse JMX Console
 * Copyright (C) 2006 Jeff Mesnil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
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
