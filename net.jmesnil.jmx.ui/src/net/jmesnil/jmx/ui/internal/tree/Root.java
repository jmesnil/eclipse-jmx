/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.tree;

import javax.management.MBeanServerConnection;

public class Root extends Node {

    private MBeanServerConnection mbsc;

    Root(MBeanServerConnection mbsc) {
        super(null);
        this.mbsc = mbsc;
    }

    @Override
    public String toString() {
        return "Root"; //$NON-NLS-1$
    }

    public int compareTo(Object o) {
        return 0;
    }

    MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

}
