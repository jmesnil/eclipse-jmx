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
 */
package net.jmesnil.jmx.resources;

import java.lang.management.ManagementFactory;
import java.util.UUID;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.TestCase;

public class DefaultMBeanServerConnectionFactoryTest extends TestCase {

    private String correctURL;

    private JMXConnectorServer cs;

    protected void setUp() throws Exception {
        super.setUp();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"); //$NON-NLS-1$
        cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
        cs.start();
        correctURL = cs.getAddress().toString();
        System.out.println(correctURL);
    }

    protected void tearDown() throws Exception {
        cs.stop();
        correctURL = null;
        super.tearDown();
    }

    public void testConnectToNullURL() throws Exception {
        DefaultMBeanServerConnectionFactory factory = new DefaultMBeanServerConnectionFactory();
        try {
            factory.createMBeanServerConnection(null);
            fail("should not connect to null descriptor"); //$NON-NLS-1$
        } catch (Exception e) {
        }
    }

    public void testConnectToBadURL() throws Exception {
        DefaultMBeanServerConnectionFactory factory = new DefaultMBeanServerConnectionFactory();
        MBeanServerConnectionDescriptor descriptor = new MBeanServerConnectionDescriptor(UUID.randomUUID().toString(), "service:whatever", null, null);
        try {
            factory.createMBeanServerConnection(descriptor);
            fail("should not connect to bad URL"); //$NON-NLS-1$
        } catch (Exception e) {
        }
    }

    public void testConnectToURL() throws Exception {
        DefaultMBeanServerConnectionFactory factory = new DefaultMBeanServerConnectionFactory();
        MBeanServerConnectionDescriptor descriptor = new MBeanServerConnectionDescriptor(correctURL, correctURL, null, null);
        MBeanServerConnection mbsc = factory.createMBeanServerConnection(descriptor);
        assertNotNull(mbsc);
    }
}
