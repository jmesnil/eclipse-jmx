/**
 * net.jmesnil.jmx.ui.test
 * Copyright (C) 2006 Jeff Mesnil
 * Contact: http://www.jmesnil.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package net.jmesnil.jmx.ui.internal.tree;

import java.io.IOException;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

final class MockMBeanServerConnection implements MBeanServerConnection {
    public void addNotificationListener(ObjectName name,
            NotificationListener listener, NotificationFilter filter,
            Object handback) throws InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub

    }

    public void addNotificationListener(ObjectName name, ObjectName listener,
            NotificationFilter filter, Object handback)
            throws InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub

    }

    public ObjectInstance createMBean(String className, ObjectName name)
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ObjectInstance createMBean(String className, ObjectName name,
            ObjectName loaderName) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException,
            InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ObjectInstance createMBean(String className, ObjectName name,
            Object[] params, String[] signature) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ObjectInstance createMBean(String className, ObjectName name,
            ObjectName loaderName, Object[] params, String[] signature)
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException, InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes)
            throws InstanceNotFoundException, ReflectionException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDefaultDomain() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getDomains() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getMBeanCount() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ObjectInstance getObjectInstance(ObjectName name)
            throws InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object invoke(ObjectName name, String operationName,
            Object[] params, String[] signature)
            throws InstanceNotFoundException, MBeanException,
            ReflectionException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isInstanceOf(ObjectName name, String className)
            throws InstanceNotFoundException, IOException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isRegistered(ObjectName name) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public Set queryMBeans(ObjectName name, QueryExp query) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public Set queryNames(ObjectName name, QueryExp query) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener)
            throws InstanceNotFoundException, ListenerNotFoundException,
            IOException {
        // TODO Auto-generated method stub

    }

    public void removeNotificationListener(ObjectName name,
            NotificationListener listener) throws InstanceNotFoundException,
            ListenerNotFoundException, IOException {
        // TODO Auto-generated method stub

    }

    public void removeNotificationListener(ObjectName name,
            ObjectName listener, NotificationFilter filter, Object handback)
            throws InstanceNotFoundException, ListenerNotFoundException,
            IOException {
        // TODO Auto-generated method stub

    }

    public void removeNotificationListener(ObjectName name,
            NotificationListener listener, NotificationFilter filter,
            Object handback) throws InstanceNotFoundException,
            ListenerNotFoundException, IOException {
        // TODO Auto-generated method stub

    }

    public void setAttribute(ObjectName name, Attribute attribute)
            throws InstanceNotFoundException, AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException,
            ReflectionException, IOException {
        // TODO Auto-generated method stub

    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes)
            throws InstanceNotFoundException, ReflectionException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public void unregisterMBean(ObjectName name)
            throws InstanceNotFoundException, MBeanRegistrationException,
            IOException {
        // TODO Auto-generated method stub

    }
}