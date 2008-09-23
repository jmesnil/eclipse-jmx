/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.core.internal.providers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jmesnil.jmx.core.IConnectionProvider;
import net.jmesnil.jmx.core.IConnectionProviderListener;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.core.IMemento;
import net.jmesnil.jmx.core.JMXActivator;
import net.jmesnil.jmx.core.JMXCoreMessages;
import net.jmesnil.jmx.core.util.XMLMemento;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The default connection type that comes bundled
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class DefaultConnectionProvider implements IConnectionProvider {
	public static final String PROVIDER_ID = "net.jmesnil.jmx.core.internal.providers.DefaultConnectionProvider"; //$NON-NLS-1$
	public static final String ID = "id"; //$NON-NLS-1$
	public static final String URL = "url"; //$NON-NLS-1$
	public static final String USERNAME = "username"; //$NON-NLS-1$
	public static final String PASSWORD = "password"; //$NON-NLS-1$
	public static final String CONNECTION = "connection";  //$NON-NLS-1$
	public static final String CONNECTIONS = "connections";  //$NON-NLS-1$
	public static final String STORE_FILE = "defaultConnections.xml"; //$NON-NLS-1$
	private ArrayList<IConnectionProviderListener> listeners =
		new ArrayList<IConnectionProviderListener>();

	public DefaultConnectionProvider() {
		addListener(new AutomaticStarter());
	}

	public String getId() {
		return PROVIDER_ID;
	}
	public void addListener(IConnectionProviderListener listener) {
		if( !listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeListener(IConnectionProviderListener listener) {
		listeners.remove(listener);
	}

	void fireAdded(IConnectionWrapper wrapper) {
		for(Iterator<IConnectionProviderListener> i = listeners.iterator(); i.hasNext();)
			try {
				i.next().connectionAdded(wrapper);
			} catch(RuntimeException re) {}
		}

	void fireChanged(IConnectionWrapper wrapper) {
		for(Iterator<IConnectionProviderListener> i = listeners.iterator(); i.hasNext();)
			try {
				i.next().connectionChanged(wrapper);
			} catch(RuntimeException re) {}
	}

	void fireRemoved(IConnectionWrapper wrapper) {
		for(Iterator<IConnectionProviderListener> i = listeners.iterator(); i.hasNext();)
			try {
				i.next().connectionRemoved(wrapper);
			} catch(RuntimeException re) {}
	}

	public boolean canCreate() {
		return true;
	}

	public boolean canDelete(IConnectionWrapper wrapper) {
		return wrapper instanceof DefaultConnectionWrapper;
	}

	public IConnectionWrapper createConnection(Map map) throws CoreException {
		String id = (String)map.get(ID);
		String url = (String)map.get(URL);
		String username = (String)map.get(USERNAME);
		String password = (String)map.get(PASSWORD);
		MBeanServerConnectionDescriptor desc = new
			MBeanServerConnectionDescriptor(id, url, username, password);
		try {
			return new DefaultConnectionWrapper(desc);
		} catch( MalformedURLException murle) {
			throw new CoreException(new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, murle.getLocalizedMessage(), murle));
		}
	}

	private HashMap<String, DefaultConnectionWrapper> connections;
	public IConnectionWrapper[] getConnections() {
		if( connections == null )
			loadConnections();
		return connections.values().toArray(new IConnectionWrapper[connections.values().size()]);
	}

	public void addConnection(IConnectionWrapper connection) {
		if( connection instanceof DefaultConnectionWrapper ) {
			MBeanServerConnectionDescriptor descriptor =
				((DefaultConnectionWrapper)connection).getDescriptor();
			connections.put(descriptor.getID(), (DefaultConnectionWrapper)connection);
			try {
				save();
				fireAdded(connection);
			} catch( IOException ioe ) {
				IStatus s = new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, JMXCoreMessages.DefaultConnection_ErrorAdding, ioe);
				JMXActivator.log(s);
			}
		}
	}

	public void removeConnection(IConnectionWrapper connection) {
		if( connection instanceof DefaultConnectionWrapper ) {
			MBeanServerConnectionDescriptor descriptor =
				((DefaultConnectionWrapper)connection).getDescriptor();
			connections.remove(descriptor.getID());
			try {
				save();
				fireRemoved(connection);
			} catch( IOException ioe ) {
				IStatus s = new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, JMXCoreMessages.DefaultConnection_ErrorRemoving, ioe);
				JMXActivator.log(s);
			}
		}
	}

	protected void loadConnections() {
		String filename = JMXActivator.getDefault().getStateLocation().append(STORE_FILE).toOSString();
		HashMap<String, DefaultConnectionWrapper> map = new HashMap<String, DefaultConnectionWrapper>();
		if( new File(filename).exists()) {
			try {
				IMemento root = XMLMemento.loadMemento(filename);
				IMemento[] child = root.getChildren(CONNECTION);
				for( int i = 0; i < child.length; i++ ) {
					String id = child[i].getString(ID);
					String url = child[i].getString(URL);
					String username = child[i].getString(USERNAME);
					String password = child[i].getString(PASSWORD);
					MBeanServerConnectionDescriptor desc = new MBeanServerConnectionDescriptor(id, url, username, password);
					try {
						DefaultConnectionWrapper connection = new DefaultConnectionWrapper(desc);
						map.put(id, connection);
					} catch( MalformedURLException murle) {
						// TODO LOG
					}
				}
				connections = map;
			} catch( IOException ioe ) {
				IStatus s = new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, JMXCoreMessages.DefaultConnection_ErrorLoading, ioe);
				JMXActivator.log(s);
			}
		} else {
			connections = map;
		}
	}

	protected void save() throws IOException {
		String filename = JMXActivator.getDefault().getStateLocation().append(STORE_FILE).toOSString();
		List<String> keys = new ArrayList<String>();
		keys.addAll(connections.keySet());
		Collections.sort(keys);
		DefaultConnectionWrapper wrapper;
		MBeanServerConnectionDescriptor descriptor;
		XMLMemento root = XMLMemento.createWriteRoot(CONNECTIONS);
		Iterator<String> i = keys.iterator();
		while(i.hasNext()) {
			 wrapper = connections.get(i.next());
			if( wrapper != null ) {
				descriptor = wrapper.getDescriptor();
				if( descriptor != null ) {
					IMemento child = root.createChild(CONNECTION);
					child.putString(ID, descriptor.getID());
					child.putString(URL, descriptor.getURL());
					child.putString(USERNAME, descriptor.getUserName());
					child.putString(PASSWORD, descriptor.getPassword());
				}
			}
		}
		root.saveToFile(filename);
	}

	public String getName(IConnectionWrapper wrapper) {
		if( wrapper instanceof DefaultConnectionWrapper ) {
			MBeanServerConnectionDescriptor desc =
				((DefaultConnectionWrapper)wrapper).getDescriptor();
			if( desc != null )
				return desc.getID();
		}
		return null;
	}
}
