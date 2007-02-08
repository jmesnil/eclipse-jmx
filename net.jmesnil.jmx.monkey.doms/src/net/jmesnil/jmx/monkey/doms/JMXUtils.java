package net.jmesnil.jmx.monkey.doms;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXUtils {

	public ScriptableMBeanServerConnection connect(String host, int port)
			throws Exception {
		String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		JMXServiceURL jmxurl = new JMXServiceURL(url);
		JMXConnector connector = JMXConnectorFactory.connect(jmxurl);
		MBeanServerConnection mbsc = connector.getMBeanServerConnection();
		return new ScriptableMBeanServerConnection(mbsc);
	}

}
