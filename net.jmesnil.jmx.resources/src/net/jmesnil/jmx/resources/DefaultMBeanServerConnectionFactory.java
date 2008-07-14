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
