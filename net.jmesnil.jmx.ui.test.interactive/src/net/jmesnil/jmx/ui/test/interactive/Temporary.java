/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

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
