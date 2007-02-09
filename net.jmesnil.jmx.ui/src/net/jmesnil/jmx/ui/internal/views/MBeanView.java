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
 * 
 *  Code was inspired from org.eclipse.equinox.client source, (c) 2006 IBM 
 */
package net.jmesnil.jmx.ui.internal.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerConnectAction;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerDisconnectAction;
import net.jmesnil.jmx.ui.internal.tree.DomainNode;
import net.jmesnil.jmx.ui.internal.tree.Node;
import net.jmesnil.jmx.ui.internal.tree.NodeBuilder;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.tree.PropertyNode;
import net.jmesnil.jmx.ui.internal.tree.Root;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MBeanView extends ViewPart {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.MBeanView"; //$NON-NLS-1$

    private TreeViewer viewer;

    private boolean currentLayoutIsFlat = false;

    private MBeanServerConnectAction connectAction;

    private MBeanServerDisconnectAction disconnectAction;

    private LayoutActionGroup layoutActionGroup;

    protected class ViewContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            // if (child instanceof Node) {
            // Node node = (Node) child;
            // return node.getParent();
            // }
            return null;
        }

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        public Object[] getChildren(Object parent) {
            if (parent instanceof Root) {
                Root root = (Root) parent;
                return root.getChildren();
            }
            if (parent instanceof DomainNode) {
                DomainNode node = (DomainNode) parent;
                if (currentLayoutIsFlat) {
                    List objectNameNodes = findOnlyObjectNames(node);
                    return objectNameNodes.toArray();
                } else {
                    return node.getChildren();
                }
            }
            if (parent instanceof Node) {
                Node node = (Node) parent;
                return node.getChildren();
            }
            return new Object[0];
        }

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        private List findOnlyObjectNames(Node node) {
            List objectNameNodes = new ArrayList();
            Node[] children = node.getChildren();
            for (int i = 0; i < children.length; i++) {
                Node child = children[i];
                if (child instanceof ObjectNameNode) {
                    objectNameNodes.add(child);
                } else {
                    objectNameNodes.addAll(findOnlyObjectNames(child));
                }
            }
            return objectNameNodes;
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof Node) {
                Node node = (Node) parent;
                return (node.getChildren().length > 0);
            }
            return true;
        }
    }

    protected class ViewLabelProvider extends LabelProvider {

        @SuppressWarnings("unchecked")
        @Override
        public String getText(Object obj) {
            if (obj instanceof DomainNode) {
                DomainNode node = (DomainNode) obj;
                return node.getDomain();
            }
            if (obj instanceof ObjectNameNode) {
                ObjectNameNode node = (ObjectNameNode) obj;
                if (currentLayoutIsFlat) {
                    return node.getObjectName().getKeyPropertyListString();
                } else {
                    return node.getValue();
                }
            }
            if (obj instanceof PropertyNode) {
                PropertyNode node = (PropertyNode) obj;
                return node.getValue();
            }
            return obj.toString();
        }

        @Override
        public Image getImage(Object obj) {
            if (obj instanceof DomainNode) {
                return JMXImages.get(JMXImages.IMG_OBJS_LIBRARY);
            }
            if (obj instanceof ObjectNameNode) {
                return JMXImages.get(JMXImages.IMG_OBJS_INTERFACE);
            }
            if (obj instanceof PropertyNode) {
                return JMXImages.get(JMXImages.IMG_OBJS_PACKAGE);
            }
            String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
            return PlatformUI.getWorkbench().getSharedImages().getImage(
                    imageKey);
        }
    }

    /**
     * The constructor.
     */
    public MBeanView() {
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        makeActions();
        fillActionBars();
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        getViewSite().setSelectionProvider(viewer);
    }

    private void makeActions() {
        connectAction = new MBeanServerConnectAction(this);
        disconnectAction = new MBeanServerDisconnectAction(this);
        layoutActionGroup = new LayoutActionGroup(this);
        layoutActionGroup.setFlatLayout(currentLayoutIsFlat);
    }

    void fillActionBars() {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager viewMenu = actionBars.getMenuManager();

        viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        viewMenu.add(connectAction);
        viewMenu.add(disconnectAction);
        viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS
                + "-end"));//$NON-NLS-1$        

        layoutActionGroup.fillActionBars(actionBars);

        actionBars.getToolBarManager().add(connectAction);
    }

    @SuppressWarnings("unchecked")
    public void setMBeanServerConnection(MBeanServerConnectionWrapper connection) {
        try {
            Set beanInfo = connection.getMBeanServerConnection().queryNames(
                    new ObjectName("*:*"), null);
            Node root = NodeBuilder.createRoot(connection
                    .getMBeanServerConnection());
            Iterator iter = beanInfo.iterator();
            while (iter.hasNext()) {
                ObjectName on = (ObjectName) iter.next();
                NodeBuilder.addToTree(root, on);
            }
            viewer.setInput(root);
            // viewer.refresh();

        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void toggleLayout() {
        currentLayoutIsFlat = !currentLayoutIsFlat;
        viewer.getControl().setRedraw(false);
        try {
            viewer.refresh();
        } finally {
            viewer.getControl().setRedraw(true);
        }
    }

    public boolean isCurrentLayoutFlat() {
        return currentLayoutIsFlat;
    }
}