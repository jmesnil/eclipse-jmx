/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.resources;

import java.io.IOException;

import javax.management.MBeanServerConnection;

/**
 *	
 *  @author	Mitko Kolev
 *
 */
public interface MBeanServerConnectionFactory {

    MBeanServerConnection createMBeanServerConnection(MBeanServerConnectionDescriptor connectionDescriptor) throws IOException;

}
