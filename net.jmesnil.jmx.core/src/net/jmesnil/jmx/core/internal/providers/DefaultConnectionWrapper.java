/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.core.internal.providers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import net.jmesnil.jmx.core.ExtensionManager;
import net.jmesnil.jmx.core.IConnectionProvider;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.core.IJMXRunnable;
import net.jmesnil.jmx.core.JMXActivator;
import net.jmesnil.jmx.core.JMXCoreMessages;
import net.jmesnil.jmx.core.JMXException;
import net.jmesnil.jmx.core.tree.NodeUtils;
import net.jmesnil.jmx.core.tree.Root;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 *
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class DefaultConnectionWrapper implements IConnectionWrapper {
	private JMXServiceURL url;
	private JMXConnector connector;
	private MBeanServerConnection connection;
	private Root root;

	private boolean isConnected;
	private Map<String, String[]> environment;

	private MBeanServerConnectionDescriptor descriptor;
	public DefaultConnectionWrapper(MBeanServerConnectionDescriptor descriptor) throws MalformedURLException {
		this.descriptor = descriptor;
		this.isConnected = false;
        String username = descriptor.getUserName();
        environment = new HashMap<String, String[]>();
        if (username != null && username.length() > 0) {
            String[] credentials = new String[] { username, descriptor.getPassword() };
            environment.put(JMXConnector.CREDENTIALS, credentials);
        }

		url = new JMXServiceURL(descriptor.getURL());
	}

	public MBeanServerConnectionDescriptor getDescriptor() {
		return descriptor;
	}

	public IConnectionProvider getProvider() {
		return ExtensionManager.getProvider(DefaultConnectionProvider.PROVIDER_ID);
	}

	public MBeanServerConnection getConnection() {
		return connection;
	}

	public boolean canControl() {
		return true;
	}

	public synchronized void connect() throws IOException {
		// try to connect
        connector = JMXConnectorFactory.connect(url, environment);
        connection = connector.getMBeanServerConnection();
		isConnected = true;
		((DefaultConnectionProvider)getProvider()).fireChanged(this);
	}
	public synchronized void disconnect() throws IOException {
		// close
		root = null;
		connector = null;
		connection = null;
		isConnected = false;
		try {
			connector.close();
		} finally {
			((DefaultConnectionProvider)getProvider()).fireChanged(this);
		}
	}
	public boolean isConnected() {
		return isConnected;
	}
	public Root getRoot() {
		if( isConnected ) {
			if( root == null )
				try {
					root = NodeUtils.createObjectNameTree(this);
				} catch( CoreException ce ) {
					// TODO LOG
				}
		}
		return root;
	}

	public void run(IJMXRunnable runnable) throws CoreException {
		try {
			runnable.run(connection);
		} catch( JMXException ce ) {
			IStatus s = new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, JMXCoreMessages.DefaultConnection_ErrorRunningJMXCode, ce);
			throw new CoreException(s);
		}
	}
}
