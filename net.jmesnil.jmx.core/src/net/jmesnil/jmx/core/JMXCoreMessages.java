package net.jmesnil.jmx.core;

import org.eclipse.osgi.util.NLS;

public class JMXCoreMessages {
	public static String ConnectJob;
	public static String ConnectJobFailed;
	public static String DisconnectJob;
	public static String DisconnectJobFailed;
	public static String ExtensionManagerError1;
	public static String DefaultConnection_ErrorAdding;
	public static String DefaultConnection_ErrorRemoving;
	public static String DefaultConnection_ErrorLoading;
	public static String DefaultConnection_ErrorRunningJMXCode;
	static {
	    NLS.initializeMessages(JMXActivator.PLUGIN_ID + ".JMXCoreMessages", //$NON-NLS-1$
	                    JMXCoreMessages.class);
	}

}
