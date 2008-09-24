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

import net.jmesnil.jmx.core.ExtensionManager;
import net.jmesnil.jmx.core.IConnectionProviderListener;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.core.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.core.tree.DomainNode;
import net.jmesnil.jmx.core.tree.Node;
import net.jmesnil.jmx.core.tree.ObjectNameNode;
import net.jmesnil.jmx.core.tree.Root;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

/**
 * Content provider for the view
 */
public class MBeanExplorerContentProvider implements IConnectionProviderListener,
        IStructuredContentProvider, ITreeContentProvider {

	private Viewer viewer;
    public MBeanExplorerContentProvider() {
    	ExtensionManager.addConnectionProviderListener(this);
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    	this.viewer = v;
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

    public Object[] getChildren(Object parent) {
    	if( parent == null ) return new Object[] {};
		if( parent instanceof IViewPart ) {
			return ExtensionManager.getAllConnections();
		}
		if( parent instanceof IConnectionWrapper && ((IConnectionWrapper)parent).isConnected()) {
			return loadAndGetRootChildren(parent);
		}
        if (parent instanceof Root) {
            Root root = (Root) parent;
            return root.getChildren();
        }
        if (parent instanceof DomainNode) {
            DomainNode node = (DomainNode) parent;
            return node.getChildren();
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

    protected Object[] loadAndGetRootChildren(Object parent) {
		// Must load the model
		final Object parent2 = parent;
		final Root[] roots = new Root[1];
		final Boolean[] done = new Boolean[1];
		roots[0] = null;
		done[0] = new Boolean(false);
		Thread t = new Thread() {
			public void run() {
				try {
					roots[0] = ((IConnectionWrapper)parent2).getRoot();
					done[0] = new Boolean(true);
				} catch( RuntimeException re ) {
					done[0] = new Boolean(true);
				}
			}
		};
		t.start();
		while(!done[0].booleanValue()) {
			Display.getDefault().readAndDispatch();
		}
		return getChildren(roots[0]);
    }

//    @SuppressWarnings("unchecked")
//    private List findOnlyObjectNames(Node node) {
//        List objectNameNodes = new ArrayList();
//        Node[] children = node.getChildren();
//        for (int i = 0; i < children.length; i++) {
//            Node child = children[i];
//            if (child instanceof ObjectNameNode) {
//                objectNameNodes.add(child);
//            } else {
//                objectNameNodes.addAll(findOnlyObjectNames(child));
//            }
//        }
//        return objectNameNodes;
//    }

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
        if( parent instanceof IConnectionWrapper ) {
        	return ((IConnectionWrapper)parent).isConnected();
        }
        return true;
    }

	public void connectionAdded(IConnectionWrapper connection) {
		fireRefresh(connection, true);
	}

	public void connectionChanged(IConnectionWrapper connection) {
		fireRefresh(connection, false);
	}

	public void connectionRemoved(IConnectionWrapper connection) {
		fireRefresh(connection, true);
	}

	private void fireRefresh(final IConnectionWrapper connection, final boolean full) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if( viewer != null ) {
					if(full || !(viewer instanceof StructuredViewer))
						viewer.refresh();
					((StructuredViewer)viewer).refresh(connection);
				}
			}
		});
	}
}