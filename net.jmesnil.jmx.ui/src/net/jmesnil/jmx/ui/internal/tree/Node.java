/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
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
 */
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
