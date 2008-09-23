/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.core;

import javax.management.MBeanServerConnection;

/**
 * API to represent a runnable that requires a connection
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public interface IJMXRunnable {
	public void run(MBeanServerConnection connection)
		throws JMXException;
}
