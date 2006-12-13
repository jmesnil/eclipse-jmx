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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JMXCoreActivator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.jmesnil.jmx.core"; //$NON-NLS-1$

    // The shared instance
    private static JMXCoreActivator plugin;

    private JMXConnectorServer cs;

    public JMXCoreActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            System.setProperty("java.rmi.server.randomIDs", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            LocateRegistry.createRegistry(3000);
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://:3000/jmxrmi"); //$NON-NLS-1$
            cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbs);
            cs.start();
        } catch (Exception e) {
            // FIXME Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        cs.stop();
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static JMXCoreActivator getDefault() {
        return plugin;
    }

    public MBeanServerConnectionWrapper connect(String url) throws IOException {
        Assert.isNotNull(url);
        JMXServiceURL jmxurl = new JMXServiceURL(url);
        JMXConnector connector = JMXConnectorFactory.connect(jmxurl);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();
        MBeanServerConnectionWrapper wrapper = new MBeanServerConnectionWrapper(
                mbsc);
        return wrapper;
    }
}
