package net.jmesnil.jmx.ui.test.interactive;

import java.lang.management.ManagementFactory;

import javax.management.ObjectName;
import javax.management.StandardMBean;

public class Registration extends StandardMBean implements RegistrationMBean {

    private ObjectName tempOn;

    public Registration() throws Exception {
        super(RegistrationMBean.class);
        tempOn = ObjectName.getInstance("net.jmesnil.test:Type=Temporary");
    }

    public void registerTemporary() throws Exception {
        Temporary temp = new Temporary();
        ManagementFactory.getPlatformMBeanServer().registerMBean(temp, tempOn);
    }

    public void unregisterTemporary() throws Exception {
        ManagementFactory.getPlatformMBeanServer().unregisterMBean(tempOn);
    }

}
