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

import junit.framework.TestCase;

public class NodeBuilderTestCase extends TestCase {

    private MBeanServerConnection mockConn;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockConn = new MockMBeanServerConnection();
    }

    @Override
    protected void tearDown() throws Exception {
        mockConn = null;
        super.tearDown();
    }

    public void testOneObjectName() throws Exception {
        ObjectName on = new ObjectName("test:type=Test,name=Test1");

        Node root = NodeBuilder.createRoot(mockConn);
        NodeBuilder.addToTree(root, on);

        Node[] children = root.getChildren();
        assertEquals(1, children.length);
        assertTrue(children[0] instanceof DomainNode);
        DomainNode domainNode = (DomainNode) children[0];
        assertEquals("test", domainNode.getDomain());

        children = domainNode.getChildren();
        assertEquals(1, children.length);
        assertTrue(children[0] instanceof PropertyNode);
        PropertyNode typeNode = (PropertyNode) children[0];
        assertEquals("type", typeNode.getKey());
        assertEquals("Test", typeNode.getValue());

        children = typeNode.getChildren();
        assertEquals(1, children.length);
        assertTrue(children[0] instanceof ObjectNameNode);
        ObjectNameNode onNode = (ObjectNameNode) children[0];
        assertEquals(on, onNode.getObjectName());
    }

    public void testTwoObjectNames() throws Exception {
        ObjectName on = new ObjectName("test:type=Test,name=Test1");
        ObjectName on2 = new ObjectName("test:type=Test,name=Test2");

        Node root = NodeBuilder.createRoot(mockConn);
        NodeBuilder.addToTree(root, on);
        NodeBuilder.addToTree(root, on2);

        Node[] children = root.getChildren();
        assertEquals(1, children.length);
        assertTrue(children[0] instanceof DomainNode);
        DomainNode domainNode = (DomainNode) children[0];
        assertEquals("test", domainNode.getDomain());

        children = domainNode.getChildren();
        assertEquals(1, children.length);
        assertTrue(children[0] instanceof PropertyNode);
        PropertyNode typeNode = (PropertyNode) children[0];
        assertEquals("type", typeNode.getKey());
        assertEquals("Test", typeNode.getValue());

        children = typeNode.getChildren();
        assertEquals(2, children.length);
        assertTrue(children[0] instanceof ObjectNameNode);
        assertTrue(children[1] instanceof ObjectNameNode);
        ObjectNameNode onNode = (ObjectNameNode) children[0];
        ObjectNameNode onNode2 = (ObjectNameNode) children[1];
        assertEquals(on, onNode.getObjectName());
        assertEquals(on2, onNode2.getObjectName());
    }

    public void testTwoDifferentDomains() throws Exception {
        ObjectName on = new ObjectName("test:type=Test,name=Test1");
        ObjectName other = new ObjectName("other:type=Test,name=Test2");

        Node root = NodeBuilder.createRoot(mockConn);
        NodeBuilder.addToTree(root, on);
        NodeBuilder.addToTree(root, other);

        Node[] children = root.getChildren();
        assertEquals(2, children.length);
        assertTrue(children[0] instanceof DomainNode);
        assertTrue(children[1] instanceof DomainNode);
        DomainNode domainNode1 = (DomainNode) children[0];
        DomainNode domainNode2 = (DomainNode) children[1];
        // domains are sorted by lexical order
        assertEquals("other", domainNode1.getDomain());
        assertEquals("test", domainNode2.getDomain());
    }

    public void testHierarchy() throws Exception {
        ObjectName on = new ObjectName("test:type=Test,name=Test1");
        ObjectName on2 = new ObjectName("test:type=Test,name=Test2");
        ObjectName on3 = new ObjectName("test:type=AnotherTest,name=Test1");
        ObjectName on4 = new ObjectName("test:type=AnotherTest,name=Test2");
        ObjectName on5 = new ObjectName("other:type=Test,name=Test1");

        Node root = NodeBuilder.createRoot(mockConn);
        NodeBuilder.addToTree(root, on);
        NodeBuilder.addToTree(root, on2);
        NodeBuilder.addToTree(root, on3);
        NodeBuilder.addToTree(root, on4);
        NodeBuilder.addToTree(root, on5);
    }
}
