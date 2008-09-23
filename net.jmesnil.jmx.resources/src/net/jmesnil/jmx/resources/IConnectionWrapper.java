/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.resources;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

import net.jmesnil.jmx.resources.tree.Root;

/**
 * API for a connection wrapper
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public interface IConnectionWrapper {
	public IConnectionProvider getProvider();
	public boolean isConnected();
	public boolean canControl();
	public void connect() throws IOException;
	public void disconnect() throws IOException;
	public Root getRoot();
	public void run(IJMXRunnable runnable) throws CoreException;
}
