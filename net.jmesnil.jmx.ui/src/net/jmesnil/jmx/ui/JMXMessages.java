/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.ui;

import org.eclipse.osgi.util.NLS;

public class JMXMessages extends NLS {
	public static String OpenJMXConnectionJob;
	public static String OpenJMXConnectionError;
	public static String CloseJMXConnectionJob;
	public static String CloseJMXConnectionError;
	public static String NewConnectionAction;
	public static String NewConnectionWizard;
	public static String DefaultConnectionWizardPage_Description;
	public static String DefaultConnectionWizardPage_Simple;
	public static String DefaultConnectionWizardPage_Advanced;
	public static String DefaultConnectionWizardPage_Name;
	public static String DefaultConnectionWizardPage_Default_Name;
	public static String DefaultConnectionWizardPage_Host;
	public static String DefaultConnectionWizardPage_Port;
	public static String DefaultConnectionWizardPage_Username;
	public static String DefaultConnectionWizardPage_Password;
	public static String DefaultConnectionWizardPage_JMX_URL;

	static {
	    NLS.initializeMessages(JMXUIActivator.PLUGIN_ID + ".JMXMessages", //$NON-NLS-1$
	                    JMXMessages.class);
	}
}
