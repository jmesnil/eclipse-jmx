package net.jmesnil.jmx.core.internal.providers;

import net.jmesnil.jmx.core.ConnectJob;
import net.jmesnil.jmx.core.IConnectionProviderListener;
import net.jmesnil.jmx.core.IConnectionWrapper;

public class AutomaticStarter implements IConnectionProviderListener {

	public void connectionAdded(IConnectionWrapper connection) {
		if( connection instanceof DefaultConnectionWrapper ) {
			new ConnectJob(connection).schedule();
		}
	}

	public void connectionChanged(IConnectionWrapper connection) {
	}
	public void connectionRemoved(IConnectionWrapper connection) {
	}

}
