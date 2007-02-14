package net.jmesnil.jmx.ui.test.interactive;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class Temporary extends StandardMBean implements TemporaryMBean {

    public Temporary() throws NotCompliantMBeanException {
        super(TemporaryMBean.class);
    }

    public void foo() {
        System.out.println("Temporary.foo()");
    }
}
