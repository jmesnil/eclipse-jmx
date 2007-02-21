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
package net.jmesnil.jmx.ui.internal.views.explorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerConnectAction;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerDisconnectAction;
import net.jmesnil.jmx.ui.internal.tree.DomainNode;
import net.jmesnil.jmx.ui.internal.tree.Node;
import net.jmesnil.jmx.ui.internal.tree.NodeBuilder;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.tree.PropertyNode;
import net.jmesnil.jmx.ui.internal.tree.Root;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

public class MBeanExplorer extends ViewPart {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer"; //$NON-NLS-1$

    private TreeViewer viewer;

    private boolean currentLayoutIsFlat = false;

    private Action collapseAllAction;

    private Action connectAction;

    private Action disconnectAction;

    private LayoutActionGroup layoutActionGroup;

    private NotificationListener registrationListener;

    private MBeanServerConnectionWrapper wrapper;

    private final class CollapseAllAction extends Action {

        public CollapseAllAction() {
            setText(Messages.CollapseAllAction_text);
            JMXImages.setLocalImageDescriptors(this, "collapseall.gif"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            viewer.collapseAll();
        }
    }

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
            if (child instanceof Node) {
                Node node = (Node) child;
                return node.getParent();
            }
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

        @SuppressWarnings("unchecked")//$NON-NLS-1$
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
                return JMXImages.get(JMXImages.IMG_OBJS_METHOD);
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
    public MBeanExplorer() {
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        makeActions();
        fillActionBars();
        FilteredTree filter = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL, new PatternFilter());
        viewer = filter.getViewer();
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = event.getSelection();
                StructuredSelection structured = (StructuredSelection) selection;
                Object element = structured.getFirstElement();
                boolean expanded = viewer.getExpandedState(element);
                if (expanded) {
                    viewer.collapseToLevel(element, 1);
                } else {
                    viewer.expandToLevel(element, 1);
                }
            }
        });
        getViewSite().setSelectionProvider(viewer);
    }

    @Override
    public void dispose() {
        removeRegistrationListener();
        super.dispose();
    }

    private void makeActions() {
        collapseAllAction = new CollapseAllAction();
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
        actionBars.getToolBarManager().add(new Separator());
        actionBars.getToolBarManager().add(collapseAllAction);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setMBeanServerConnection(MBeanServerConnectionWrapper wrapper) {
        if (wrapper == null) {
            removeRegistrationListener();
            viewer.setInput(null);
            return;
        }
        this.wrapper = wrapper;
        MBeanServerConnection mbsc = wrapper.getMBeanServerConnection();
        addRegistrationListener(mbsc);
        try {
            Node root = createObjectNameTree(mbsc);
            viewer.setInput(root);
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
        }
    }

    private void addRegistrationListener(MBeanServerConnection mbsc) {
        registrationListener = new NotificationListener() {
            public void handleNotification(Notification notifcation,
                    Object handback) {
                if (notifcation instanceof MBeanServerNotification) {
                    try {
                        MBeanServerConnection mbsc = (MBeanServerConnection) handback;
                        final Node root = createObjectNameTree(mbsc);
                        viewer.getControl().getDisplay().syncExec(
                                new Runnable() {
                                    public void run() {
                                        viewer.setInput(root);
                                    }
                                });
                    } catch (Exception e) {
                        JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
                    }
                }
            }
        };
        try {
            ObjectName mbeanServerON = ObjectName
                    .getInstance("JMImplementation:type=MBeanServerDelegate"); //$NON-NLS-1$
            mbsc.addNotificationListener(mbeanServerON, registrationListener,
                    null, mbsc);
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
        }
    }

    private void removeRegistrationListener() {
        if (wrapper != null) {
            MBeanServerConnection mbsc = wrapper.getMBeanServerConnection();
            try {
                ObjectName mbeanServerON = ObjectName
                        .getInstance("JMImplementation:type=MBeanServerDelegate"); //$NON-NLS-1$
                mbsc.removeNotificationListener(mbeanServerON,
                        registrationListener);
            } catch (Exception e) {
                JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Node createObjectNameTree(Object handback) throws IOException,
            MalformedObjectNameException {
        MBeanServerConnection connection = (MBeanServerConnection) handback;
        Set beanInfo = connection.queryNames(new ObjectName("*:*"), null); //$NON-NLS-1$
        final Node root = NodeBuilder.createRoot(connection);
        Iterator iter = beanInfo.iterator();
        while (iter.hasNext()) {
            ObjectName on = (ObjectName) iter.next();
            NodeBuilder.addToTree(root, on);
        }
        return root;
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