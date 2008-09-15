/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.adapters;

import net.jmesnil.jmx.resources.DomainWrapper;
import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

public class JMXAdapterFactory implements IAdapterFactory {

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IPropertySource.class) {
            return adaptToPropertySource(adaptableObject);
        }
        return null;
    }

    private IPropertySource adaptToPropertySource(Object adaptableObject) {
        if (adaptableObject instanceof DomainWrapper) {
            DomainWrapper domain = (DomainWrapper) adaptableObject;
            return new DomainPropertySourceAdapter(domain.getName());
        }
        if (adaptableObject instanceof MBeanInfoWrapper) {
            MBeanInfoWrapper wrapper = (MBeanInfoWrapper) adaptableObject;
            return new MBeanInfoPropertySourceAdapter(wrapper.getObjectName(),
                    wrapper.getMBeanInfo());
        }
        if (adaptableObject instanceof MBeanAttributeInfoWrapper) {
            MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) adaptableObject;
            return new MBeanAttributeInfoPropertySourceAdapter(wrapper
                    .getMBeanAttributeInfo(), wrapper.getObjectName(), wrapper
                    .getMBeanServerConnection());
        }
        if (adaptableObject instanceof MBeanOperationInfoWrapper) {
            MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) adaptableObject;
            return new MBeanOperationInfoPropertySourceAdapter(wrapper
                    .getMBeanOperationInfo());
        }
        return null;
    }
}
