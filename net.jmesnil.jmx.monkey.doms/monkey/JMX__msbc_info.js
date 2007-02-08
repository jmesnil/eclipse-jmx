/*
 * Menu: JMX > Get MBeanServer Information
 * DOM: http://eclipse-jmx.googlecode.com/svn/trunk/net.jmesnil.jmx.update/net.jmesnil.jmx.monkey.doms
 */
 
function main() {
    var text = "";
    
    var mbsc = jmx.connect("localhost", 3000);
    text = "Domains:\n\n";
    var realMbsc = mbsc.getObject();
    var domains = realMbsc.getDomains();
    for each(domain in domains) {
	    text += "- " + domain + "\n";
    }
    var mbeanCount = realMbsc.getMBeanCount();
    text += "\n" + mbeanCount + " registered MBeans"; 
    
    Packages.org.eclipse.jface.dialogs.MessageDialog.openInformation( 	
	    window.getShell(), 	
	    "MBeanServer Information", 
	    text	
	)  
}
