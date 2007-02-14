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
package net.jmesnil.jmx.ui.test.interactive;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.jmesnil.jmx.ui.test.interactive"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private JMXConnectorServer cs;

    /**
     * The constructor
     */
    public Activator() {
        plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(new ArrayType(), ObjectName
                .getInstance("net.jmesnil.test:type=ArrayType")); //$NON-NLS-1$
        mbs.registerMBean(new WritableAttributes(), ObjectName
                .getInstance("net.jmesnil.test:type=WritableAttributes")); //$NON-NLS-1$
        mbs.registerMBean(new ComplexType(), ObjectName
                .getInstance("net.jmesnil.test:type=ComplexType")); //$NON-NLS-1$
        mbs.registerMBean(new OperationResults(), ObjectName
                .getInstance("net.jmesnil.test:type=OperationResults")); //$NON-NLS-1$
        mbs.registerMBean(new Registration(), ObjectName
                .getInstance("net.jmesnil.test:type=Registration")); //$NON-NLS-1$
        try {
            System.setProperty("java.rmi.server.randomIDs", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            LocateRegistry.createRegistry(3000);
            JMXServiceURL url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://:3000/jmxrmi"); //$NON-NLS-1$
            cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbs);
            cs.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.unregisterMBean(ObjectName
                .getInstance("net.jmesnil.test:type=ArrayType")); //$NON-NLS-1$
        mbs.unregisterMBean(ObjectName
                .getInstance("net.jmesnil.test:type=WritableAttributes")); //$NON-NLS-1$
        mbs.unregisterMBean(ObjectName
                .getInstance("net.jmesnil.test:type=ComplexType")); //$NON-NLS-1$
        mbs.unregisterMBean(ObjectName
                .getInstance("net.jmesnil.test:type=OperationResults")); //$NON-NLS-1$
        mbs.unregisterMBean(ObjectName
                .getInstance("net.jmesnil.test:type=Registration")); //$NON-NLS-1$
        cs.stop();
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public void earlyStartup() {
        Activator.getDefault();
    }
}
