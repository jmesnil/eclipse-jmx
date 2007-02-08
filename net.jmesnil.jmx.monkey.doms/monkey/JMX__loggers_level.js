/*
 * Menu: JMX > Update Logging level
 * DOM: http://eclipse-jmx.googlecode.com/svn/trunk/net.jmesnil.jmx.update/net.jmesnil.jmx.monkey.doms
 */
 
function main() {
    var level = "INFO";
    
    var mbsc = jmx.connect("localhost", 3000);
    var loggingMBean = mbsc.getMBean("java.util.logging:type=Logging");

	var text = "";	
    for each(loggerName in loggingMBean.LoggerNames) {
        mbsc.invoke(loggingMBean, "setLoggerLevel", 
             [loggerName, level],
             ["java.lang.String", "java.lang.String"]);
		text += "- " + loggerName + " set to " + level + "\n";
    }	
    
    Packages.org.eclipse.jface.dialogs.MessageDialog.openInformation( 	
	    window.getShell(), 	
	    "Update Logging", 
	    text	
	)  
}
