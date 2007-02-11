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

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class NodeBuilder {

    public static void addToList(Node root, ObjectName on) {
        Node node = buildDomainNode(root, on.getDomain());
        node = buildObjectNameNode(node, "on", on.getKeyPropertyListString(), on); //$NON-NLS-1$
    }

    public static void addToTree(Node root, ObjectName on) {
        Node node = buildDomainNode(root, on.getDomain());
        String keyPropertyListString = on.getKeyPropertyListString();
        String[] properties = keyPropertyListString.split(","); //$NON-NLS-1$
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];
            String key = property.substring(0, property.indexOf('='));
            String value = property.substring(property.indexOf('=') + 1,
                    property.length());
            if (i == properties.length - 1) {
                node = buildObjectNameNode(node, key, value, on);
            } else {
                node = buildPropertyNode(node, key, value);
            }
        }
    }

    public static Node createRoot(MBeanServerConnection mbsc) {
        return new Root(mbsc);
    }

    static Node buildDomainNode(Node parent, String domain) {
        Node n = new DomainNode(parent, domain);
        if (parent != null) {
            return parent.addChildren(n);
        }
        return n;
    }

    static Node buildPropertyNode(Node parent, String key, String value) {
        Node n = new PropertyNode(parent, key, value);
        if (parent != null) {
            return parent.addChildren(n);
        }
        return n;
    }

    static Node buildObjectNameNode(Node parent, String key, String value,
            ObjectName on) {
        Node n = new ObjectNameNode(parent, key, value, on);
        if (parent != null) {
            return parent.addChildren(n);
        }
        return n;
    }

}
