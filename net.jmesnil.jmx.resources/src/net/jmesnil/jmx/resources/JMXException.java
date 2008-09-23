/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.resources;

/**
 * An exception occurring during a JMX query
 * @author "Rob Stryker"<rob.stryker@redhat.com>
 *
 */
public class JMXException extends Exception {
	private static final long serialVersionUID = 1L;

	public JMXException(Exception e) {
		super(e);
	}
}