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

import net.jmesnil.jmx.resources.MBeanServerConnectionFactory;
import net.jmesnil.jmx.resources.DefaultMBeanServerConnectionFactory;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class JMXCoreActivator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.jmesnil.jmx.core"; //$NON-NLS-1$

    // The shared instance
    private static JMXCoreActivator plugin;

    private ServiceRegistration connectionFactoryRegistration;
    public JMXCoreActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        connectionFactoryRegistration =  context.registerService(MBeanServerConnectionFactory.class.getName(), new DefaultMBeanServerConnectionFactory(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        if (connectionFactoryRegistration!= null)
        	connectionFactoryRegistration.unregister();
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static JMXCoreActivator getDefault() {
        return plugin;
    }
}
