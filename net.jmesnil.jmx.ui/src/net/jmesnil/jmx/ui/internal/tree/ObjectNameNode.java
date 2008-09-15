/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.tree;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;

public class ObjectNameNode extends PropertyNode {

    private ObjectName on;

    private MBeanInfoWrapper wrapper;

    public ObjectNameNode(Node parent, String key, String value, ObjectName on) {
        super(parent, key, value);
        Root root = getRoot(parent);
        MBeanServerConnection mbsc = root.getMBeanServerConnection();
        this.on = on;
        try {
            this.wrapper = new MBeanInfoWrapper(on, mbsc.getMBeanInfo(on), mbsc);
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
                    Messages.ObjectNameNode_error, on.getCanonicalName()), e);
        }
    }

    public ObjectName getObjectName() {
        return on;
    }

    public MBeanInfoWrapper getMbeanInfoWrapper() {
        return wrapper;
    }

    @Override
    public String toString() {
        return "ObjectNameNode[on=" + on.getKeyPropertyListString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((on == null) ? 0 : on.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof ObjectNameNode))
            return false;
        final ObjectNameNode other = (ObjectNameNode) obj;
        if (on == null) {
            if (other.on != null)
                return false;
        } else if (!on.equals(other.on))
            return false;
        return true;
    }

}
