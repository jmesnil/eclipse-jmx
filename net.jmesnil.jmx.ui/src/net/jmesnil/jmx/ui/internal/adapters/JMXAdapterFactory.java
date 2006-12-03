/**
 * Eclipse JMX Console
 * Copyright (C) 2006 Jeff Mesnil
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
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

    /**
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
     *      java.lang.Class)
     */
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
