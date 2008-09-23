/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.jmesnil.jmx.resources.tree;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import net.jmesnil.jmx.resources.IConnectionWrapper;
import net.jmesnil.jmx.resources.IJMXRunnable;
import net.jmesnil.jmx.resources.JMXException;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;

import org.eclipse.core.runtime.CoreException;

public class ObjectNameNode extends PropertyNode {

    private ObjectName on;

    private MBeanInfoWrapper wrapper;

    public ObjectNameNode(Node parent, String key, String value, ObjectName on) {
        super(parent, key, value);
        Root root = getRoot(parent);
        IConnectionWrapper connectionWrapper = root.getConnection();
        this.on = on;
    	final MBeanInfoWrapper[] array = new MBeanInfoWrapper[1];
    	final ObjectName on2 = on;
    	try {
	    	connectionWrapper.run(new IJMXRunnable() {
	    		public void run(MBeanServerConnection mbsc) throws JMXException {
					try {
						array[0] = new MBeanInfoWrapper(on2, mbsc.getMBeanInfo(on2), mbsc);
					} catch (InstanceNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IntrospectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ReflectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	});
    	} catch( CoreException ce ) {
    		// TODO LOG
    	}
    	wrapper = array[0];
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
