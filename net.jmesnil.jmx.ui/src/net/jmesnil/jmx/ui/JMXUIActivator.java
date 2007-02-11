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
package net.jmesnil.jmx.ui;

import net.jmesnil.jmx.resources.DomainWrapper;
import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.internal.adapters.JMXAdapterFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JMXUIActivator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.jmesnil.jmx.ui"; //$NON-NLS-1$

    // The shared instance
    private static JMXUIActivator plugin;

    private JMXAdapterFactory adapterFactory;

    /**
     * The constructor
     */
    public JMXUIActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        registerAdapters();
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        unregisterAdapters();
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static JMXUIActivator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    /**
     * Log the given exception along with the provided message and severity
     * indicator
     */
    public static void log(int severity, String message, Throwable e) {
        log(new Status(severity, PLUGIN_ID, 0, message, e));
    }

    private void registerAdapters() {
        adapterFactory = new JMXAdapterFactory();
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                DomainWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanInfoWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanAttributeInfoWrapper.class);
        Platform.getAdapterManager().registerAdapters(adapterFactory,
                MBeanOperationInfoWrapper.class);
    }

    private void unregisterAdapters() {
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                DomainWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanInfoWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanAttributeInfoWrapper.class);
        Platform.getAdapterManager().unregisterAdapters(adapterFactory,
                MBeanOperationInfoWrapper.class);
    }
}
