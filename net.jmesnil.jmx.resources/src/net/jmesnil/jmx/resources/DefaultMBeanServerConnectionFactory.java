/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.eclipse.core.runtime.Assert;

/**
 *  @author	Mitko Kolev (i000174)
 */
public class DefaultMBeanServerConnectionFactory implements MBeanServerConnectionFactory {

	/* (non-Javadoc)
	 * @see net.jmesnil.jmx.resources.ConnectionFactory#connect(net.jmesnil.jmx.resources.MBeanConnectionDescriptor)
	 */
    @SuppressWarnings("unchecked")
    public MBeanServerConnection createMBeanServerConnection (
            MBeanServerConnectionDescriptor descriptor) throws IOException{
        Assert.isNotNull(descriptor);
  
        String username = descriptor.getUserName();
        Map env = new HashMap();
        if (username != null && username.length() > 0) {
            String[] credentials = new String[] { username, descriptor.getPassword() };
            env.put(JMXConnector.CREDENTIALS, credentials);
        }

        JMXServiceURL jmxurl = new JMXServiceURL(descriptor.getURL());
        JMXConnector connector = JMXConnectorFactory.connect(jmxurl, env);
        return connector.getMBeanServerConnection();
    }

}
