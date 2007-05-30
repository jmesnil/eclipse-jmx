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
