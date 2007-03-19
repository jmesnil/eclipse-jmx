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
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

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

    private MBeanServerConnection mbsc;

    public JMXCoreActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
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

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public MBeanServerConnection connect(String url, String userName,
            String password) throws IOException {
        Assert.isNotNull(url);
        Assert.isNotNull(userName);
        Assert.isNotNull(password);
        Map env = new HashMap();
        if (userName.length() > 0) {
            String[] credentials = new String[] { userName, password };
            env.put(JMXConnector.CREDENTIALS, credentials);
        }

        JMXServiceURL jmxurl = new JMXServiceURL(url);
        JMXConnector connector = JMXConnectorFactory.connect(jmxurl, env);
        mbsc = connector.getMBeanServerConnection();
        return mbsc;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }
}
