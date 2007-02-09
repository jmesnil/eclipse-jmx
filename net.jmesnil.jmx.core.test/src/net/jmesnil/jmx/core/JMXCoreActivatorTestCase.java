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
package net.jmesnil.jmx.core;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.TestCase;
import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;

public class JMXCoreActivatorTestCase extends TestCase {

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
        JMXCoreActivator activator = JMXCoreActivator.getDefault();
        try {
            activator.connect(null, null, null);
            fail("should not connect to null URL"); //$NON-NLS-1$
        } catch (Exception e) {
        }
    }

    public void testConnectToBadURL() throws Exception {
        JMXCoreActivator activator = JMXCoreActivator.getDefault();
        try {
            activator.connect("service:whatever", null, null); //$NON-NLS-1$
            fail("should not connect to bad URL"); //$NON-NLS-1$
        } catch (Exception e) {
        }
    }

    public void testConnectToURL() throws Exception {
        JMXCoreActivator activator = JMXCoreActivator.getDefault();
        MBeanServerConnectionWrapper wrapper = activator.connect(correctURL, null, null);
        assertNotNull(wrapper);
        assertNotNull(wrapper.getMBeanServerConnection());
    }
}
