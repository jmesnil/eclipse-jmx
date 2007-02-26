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

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MBeanInfoPropertySourceAdapter implements IPropertySource {

    private ObjectName on;

    private MBeanInfo info;

    public MBeanInfoPropertySourceAdapter(ObjectName on, MBeanInfo info) {
        this.on = on;
        this.info = info;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        // General properties
        addDescriptor(
                "objectname", Messages.objectName, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "description", Messages.description, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "classname", Messages.className, Messages.general, descriptors); //$NON-NLS-1$
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    private void addDescriptor(String id, String displayName, String category,
            List<PropertyDescriptor> descriptors) {
        PropertyDescriptor descriptor = new PropertyDescriptor(id, displayName);
        descriptor.setCategory(category);
        descriptors.add(descriptor);
    }

    public Object getPropertyValue(Object id) {
        if ("objectname".equals(id)) { //$NON-NLS-1$
            return on.getCanonicalName();
        }
        if ("description".equals(id)) { //$NON-NLS-1$
            return info.getDescription();
        }
        if ("classname".equals(id)) { //$NON-NLS-1$
            return info.getClassName();
        }
        return null;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }

}