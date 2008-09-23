/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked") //$NON-NLS-1$
public abstract class Node implements Comparable {

	Node parent;

	@SuppressWarnings("unchecked") //$NON-NLS-1$
    List children = new ArrayList();

	Node(Node parent) {
		this.parent = parent;
	}

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	Node addChildren(Node node) {
		if (!children.contains(node)) {
			children.add(node);
			Collections.sort(children);
			return node;
		} else {
			return (Node) children.get(children.indexOf(node));
		}
	}

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public Node[] getChildren() {
		return (Node[]) children.toArray(new Node[children.size()]);
	}

	public Node getParent() {
		return parent;
	}
    
	Root getRoot(Node parent) {
        if (parent.getParent() == null) {
            return (Root) parent;
        }
	    return getRoot(parent.getParent());
    }

}
