/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

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
