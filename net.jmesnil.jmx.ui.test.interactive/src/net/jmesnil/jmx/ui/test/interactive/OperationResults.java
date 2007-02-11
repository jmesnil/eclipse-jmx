package net.jmesnil.jmx.ui.test.interactive;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class OperationResults extends StandardMBean implements
        OperationResultsMBean {

    public OperationResults() throws NotCompliantMBeanException {
        super(OperationResultsMBean.class);
    }
    
    public int[] intsOperation() {
        return new int[] {-3, -2, -1, 0, 1, 2, 3};
    }

    public String stringOperation() {
        return "operation returned a String"; //$NON-NLS-1$
    }

    public void voidOperation() {
        // do nothing
    }

}
