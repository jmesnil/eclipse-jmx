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

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.EditorUtils;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerConnectAction;
import net.jmesnil.jmx.ui.internal.actions.MBeanServerDisconnectAction;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditor;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditorInput;
import net.jmesnil.jmx.ui.internal.tree.DomainNode;
import net.jmesnil.jmx.ui.internal.tree.Node;
import net.jmesnil.jmx.ui.internal.tree.NodeBuilder;
import net.jmesnil.jmx.ui.internal.tree.NodeUtils;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.tree.PropertyNode;
import net.jmesnil.jmx.ui.internal.tree.Root;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

public class MBeanExplorer extends ViewPart {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer"; //$NON-NLS-1$

    private static final int HIERARCHICAL_LAYOUT= 0x1;
    private static final int FLAT_LAYOUT= 0x2;
    
    private static final String TAG_LAYOUT= "layout"; //$NON-NLS-1$
   private static final String TAG_LINKED_WITH_EDITOR = "linkedWithEditor"; //$NON-NLS-1$

    private TreeViewer viewer;

    private boolean currentLayoutIsFlat = false;
    private boolean linkingIsEnabled = false;

    private Action collapseAllAction;

    private Action connectAction;

    private Action disconnectAction;

    private LayoutActionGroup layoutActionGroup;

    private Action linkWithEditorAction;

    private NotificationListener registrationListener;

    private ISelectionChangedListener postSelectionListener;

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

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
        public void dispose() {}

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

    private IPartListener2 linkWithEditorListener = new IPartListener2() {
        public void partVisible(IWorkbenchPartReference partRef) {}
        public void partBroughtToTop(IWorkbenchPartReference partRef) {}
        public void partClosed(IWorkbenchPartReference partRef) {}
        public void partDeactivated(IWorkbenchPartReference partRef) {}
        public void partHidden(IWorkbenchPartReference partRef) {}
        public void partOpened(IWorkbenchPartReference partRef) {}

        public void partInputChanged(IWorkbenchPartReference partRef) {
            if (partRef instanceof IEditorReference) {
                editorActivated(((IEditorReference) partRef).getEditor(true));
            }
        }

        public void partActivated(IWorkbenchPartReference partRef) {
            if (partRef instanceof IEditorReference) {
                editorActivated(((IEditorReference) partRef).getEditor(true));
            }
        }
    };

    public MBeanExplorer() {
        postSelectionListener = new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handlePostSelectionChanged(event);
            }
        };
    }

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        restoreLayoutState(memento);
        restoreLinkWithEditorState(memento);
    }

    @Override
    public void saveState(IMemento memento) {
        saveLinkWithEditorState(memento);
        saveLayoutState(memento);
    }

    @Override
    public void createPartControl(Composite parent) {
        makeActions();
        fillActionBars();
        PatternFilter patternFilter = new PatternFilter() {
            protected boolean isLeafMatch(Viewer viewer, Object element) {
                if (element instanceof PropertyNode) {
                    PropertyNode propNode = (PropertyNode) element;
                    return wordMatches(propNode.getKey() + "=" //$NON-NLS-1$
                            + propNode.getValue());
                }
                return super.isLeafMatch(viewer, element);
            }

            public boolean isElementVisible(Viewer viewer, Object element) {
                return matchesObjectName((Node) element);
            }

            private boolean matchesObjectName(Node node) {
                if (node instanceof ObjectNameNode) {
                    ObjectNameNode onNode = (ObjectNameNode) node;
                    return wordMatches(onNode.getObjectName().toString());
                }
                boolean hasMatchingChildren = false;
                Node[] children = node.getChildren();
                for (int i = 0; i < children.length; i++) {
                    Node child = children[i];
                    hasMatchingChildren |= matchesObjectName(child);
                }
                return hasMatchingChildren;
            }
        };
        patternFilter.setIncludeLeadingWildcard(true);

        final FilteredTree filter = new FilteredTree(parent, SWT.MULTI
                | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter);

        viewer = filter.getViewer();
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                ISelection selection = event.getSelection();
                StructuredSelection structured = (StructuredSelection) selection;
                Object element = structured.getFirstElement();
                IEditorInput editorInput = EditorUtils.getEditorInput(element);
                if (editorInput != null) {
                    IWorkbenchPage page = getViewSite().getPage();
                    try {
                        page.openEditor(editorInput, MBeanEditor.ID);
                    } catch (PartInitException e) {
                        JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
                    }
                } else {
                    boolean expanded = viewer.getExpandedState(element);
                    if (expanded) {
                        viewer.collapseToLevel(element, 1);
                    } else {
                        viewer.expandToLevel(element, 1);
                    }
                }
            }
        });
        viewer.addPostSelectionChangedListener(postSelectionListener);
        getViewSite().setSelectionProvider(viewer);
    }

    @Override
    public void dispose() {
        removeRegistrationListener();
        // always remove even if we didn't register
        getSite().getPage().removePartListener(linkWithEditorListener);
        super.dispose();
    }

    private void makeActions() {
        collapseAllAction = new CollapseAllAction();
        connectAction = new MBeanServerConnectAction(this);
        disconnectAction = new MBeanServerDisconnectAction(this);
        layoutActionGroup = new LayoutActionGroup(this);
        layoutActionGroup.setFlatLayout(currentLayoutIsFlat);
        linkWithEditorAction = new Action("Link with Editor",
                Action.AS_CHECK_BOX) {
            @Override
            public void run() {
                setLinkingEnabled(linkWithEditorAction.isChecked());
            }
        };
        JMXImages.setLocalImageDescriptors(linkWithEditorAction, "synced.gif"); //$NON-NLS-1$
        linkWithEditorAction.setChecked(linkingIsEnabled);
        linkWithEditorAction.run();
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
        viewMenu.add(new Separator());
        viewMenu.add(linkWithEditorAction);

        actionBars.getToolBarManager().add(connectAction);
        actionBars.getToolBarManager().add(new Separator());
        actionBars.getToolBarManager().add(collapseAllAction);
        actionBars.getToolBarManager().add(linkWithEditorAction);
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
        saveLayoutState(null);
        viewer.getControl().setRedraw(false);
        try {
            viewer.refresh();
        } finally {
            viewer.getControl().setRedraw(true);
        }
    }

    private void saveLayoutState(IMemento memento) {
        if (memento != null) {
            memento.putInteger(TAG_LAYOUT, getLayoutAsInt());
        } else {
          //if memento is null save in preference store
            IPreferenceStore store= JMXUIActivator.getDefault().getPreferenceStore();
            store.setValue(TAG_LAYOUT, getLayoutAsInt());
        }
    }

    private void restoreLayoutState(IMemento memento) {
        Integer state = null;
        if (memento != null)
            state = memento.getInteger(TAG_LAYOUT);

        // If no memento try an restore from preference store
        if(state == null) {
            IPreferenceStore store= JMXUIActivator.getDefault().getPreferenceStore();
            state= new Integer(store.getInt(TAG_LAYOUT));
        }

        if (state.intValue() == FLAT_LAYOUT)
            currentLayoutIsFlat= true;
        else if (state.intValue() == HIERARCHICAL_LAYOUT)
            currentLayoutIsFlat= false;
        else
            currentLayoutIsFlat = false;
    }

    private int getLayoutAsInt() {
        if (currentLayoutIsFlat)
            return FLAT_LAYOUT;
        else
            return HIERARCHICAL_LAYOUT;
    }

    public boolean isCurrentLayoutFlat() {
        return currentLayoutIsFlat;
    }

    public boolean isLinkingEnabled() {
        return linkingIsEnabled;
    }

    public void setLinkingEnabled(boolean enabled) {
        linkingIsEnabled = enabled;
        saveLinkWithEditorState(null);
        IWorkbenchPage page = getSite().getPage();
        if (enabled) {
            page.addPartListener(linkWithEditorListener);

            IEditorPart editor = page.getActiveEditor();
            if (editor != null) {
                editorActivated(editor);
            }
        } else {
            page.removePartListener(linkWithEditorListener);
        }
    }

    private void saveLinkWithEditorState(IMemento memento) {
        int linkingIsEnabledAsInt = linkingIsEnabled ? 0 : 1;
        if (memento != null) {
            memento.putInteger(TAG_LINKED_WITH_EDITOR, linkingIsEnabledAsInt);
        } else {
            // if memento is null save in preference store
            IPreferenceStore store = JMXUIActivator.getDefault()
                    .getPreferenceStore();
            store.setValue(TAG_LINKED_WITH_EDITOR, linkingIsEnabledAsInt);
        }
    }

    private void restoreLinkWithEditorState(IMemento memento) {
        Integer state = null;
        if (memento != null)
            state = memento.getInteger(TAG_LINKED_WITH_EDITOR);

        // If no memento try an restore from preference store
        if (state == null) {
            IPreferenceStore store = JMXUIActivator.getDefault()
                    .getPreferenceStore();
            state = new Integer(store.getInt(TAG_LINKED_WITH_EDITOR));
        }

        if (state.intValue() == 0)
            linkingIsEnabled = true;
        else if (state.intValue() == 1)
            linkingIsEnabled = false;
        else
            linkingIsEnabled = false;
    }

    void editorActivated(IEditorPart editor) {
        Object input = editor.getEditorInput();
        if (input == null || !(input instanceof MBeanEditorInput)) {
            return;
        }
        MBeanEditorInput mbeanInput = (MBeanEditorInput) input;
        if (!inputIsSelected(mbeanInput)) {
            showInput(mbeanInput);
        } else {
            viewer.getTree().showSelection();
        }
    }

    private boolean inputIsSelected(MBeanEditorInput input) {
        IStructuredSelection selection = (IStructuredSelection) viewer
                .getSelection();
        if (selection.size() != 1)
            return false;
        boolean inputIsSelected = input.equals(EditorUtils.getEditorInput(selection
                .getFirstElement()));
        return inputIsSelected;
    }

    boolean showInput(MBeanEditorInput input) {
        MBeanInfoWrapper infoWrapper = input.getWrapper();
        ObjectName objectName = infoWrapper.getObjectName();
        Node root = (Node) viewer.getInput();
        ObjectNameNode node = NodeUtils.findObjectNameNode(root, objectName);
        if (node == null) {
            return false;
        } else {
            ISelection newSelection = new StructuredSelection(node);
            if (viewer.getSelection().equals(newSelection)) {
                viewer.reveal(node);
            } else {
                viewer.setSelection(newSelection, true);
            }
            return true;
        }
    }

    private void handlePostSelectionChanged(SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        if (isLinkingEnabled()) {
            linkToEditor((IStructuredSelection) selection);
        }
    }

    private void linkToEditor(IStructuredSelection selection) {
        if (!isActivePart())
            return;
        Object obj = selection.getFirstElement();

        if (selection.size() == 1) {
            IEditorPart part = EditorUtils.isOpenInEditor(obj);
            if (part != null) {
                IWorkbenchPage page = getSite().getPage();
                page.bringToTop(part);
            }
        }
    }

    private boolean isActivePart() {
        return this == getSite().getPage().getActivePart();
    }
}