/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.tree;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.eclipse.core.runtime.Assert;

public class NodeUtils {

    public static ObjectNameNode findObjectNameNode(Node node,
            ObjectName objectName) {
        Assert.isNotNull(node);

        if (node instanceof ObjectNameNode) {
            ObjectNameNode onNode = (ObjectNameNode) node;
            if (onNode.getObjectName().equals(objectName)) {
                return onNode;
            }
        }
        Node[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) {
            Node child = children[i];
            Node found = findObjectNameNode(child, objectName);
            if (found != null) {
                return (ObjectNameNode) found;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static Node createObjectNameTree(MBeanServerConnection mbsc)
            throws IOException, MalformedObjectNameException {
        Set beanInfo = mbsc.queryNames(new ObjectName("*:*"), null); //$NON-NLS-1$
        final Node root = NodeBuilder.createRoot(mbsc);
        Iterator iter = beanInfo.iterator();
        while (iter.hasNext()) {
            ObjectName on = (ObjectName) iter.next();
            NodeBuilder.addToTree(root, on);
        }
        return root;
    }
}
