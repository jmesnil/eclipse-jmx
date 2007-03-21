/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
 * Contact: http://www.jmesnil.net
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

import java.util.ArrayList;
import java.util.List;

import net.jmesnil.jmx.resources.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.ui.internal.tree.DomainNode;
import net.jmesnil.jmx.ui.internal.tree.Node;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.tree.Root;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class MBeanExplorerContentProvider implements
        IStructuredContentProvider, ITreeContentProvider {

    private boolean flatLayout;

    public MBeanExplorerContentProvider() {
    }

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
            if (flatLayout) {
                List objectNameNodes = findOnlyObjectNames(node);
                return objectNameNodes.toArray();
            } else {
                return node.getChildren();
            }
        }
        if (parent instanceof ObjectNameNode) {
            ObjectNameNode node = (ObjectNameNode) parent;
            return node.getMbeanInfoWrapper().getMBeanFeatureInfos();
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
        if (parent instanceof ObjectNameNode) {
            ObjectNameNode node = (ObjectNameNode) parent;
            return (node.getMbeanInfoWrapper().getMBeanFeatureInfos().length > 0);
        }
        if (parent instanceof Node) {
            Node node = (Node) parent;
            return (node.getChildren().length > 0);
        }
        if (parent instanceof MBeanFeatureInfoWrapper) {
            return false;
        }
        return true;
    }

    public void setFlatLayout(boolean state) {
        flatLayout = state;
    }
}