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
import java.util.Arrays;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MBeanAttributeInfoPropertySourceAdapter implements IPropertySource {

    private final MBeanAttributeInfo attrInfo;

    private final ObjectName on;

    private final MBeanServerConnection mbsc;

    public MBeanAttributeInfoPropertySourceAdapter(MBeanAttributeInfo attrInfo,
            ObjectName on, MBeanServerConnection mbsc) {
        Assert.isNotNull(attrInfo);
        Assert.isNotNull(on);
        Assert.isNotNull(mbsc);
        this.attrInfo = attrInfo;
        this.on = on;
        this.mbsc = mbsc;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        // General properties
        addDescriptor("name", Messages.name, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "description", Messages.description, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor("type", Messages.type, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "readable", Messages.readable, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "writable", Messages.writable, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor("value", Messages.value, Messages.general, descriptors); //$NON-NLS-1$
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    private void addDescriptor(String id, String displayName, String category,
            List<PropertyDescriptor> descriptors) {
        PropertyDescriptor descriptor = new PropertyDescriptor(id, displayName);
        descriptor.setCategory(category);
        descriptors.add(descriptor);
    }

    public Object getPropertyValue(Object id) {
        if ("name".equals(id)) { //$NON-NLS-1$
            return attrInfo.getName();
        }
        if ("description".equals(id)) { //$NON-NLS-1$
            return attrInfo.getDescription();
        }
        if ("type".equals(id)) { //$NON-NLS-1$
            Object obj = attrInfo.getType();
            if (obj instanceof Object[]) {
                return Arrays.asList((Object[]) obj).toString();
            }
            return obj;
        }
        if ("readable".equals(id)) { //$NON-NLS-1$
            return Boolean.valueOf(attrInfo.isReadable());
        }
        if ("writable".equals(id)) { //$NON-NLS-1$
            return Boolean.valueOf(attrInfo.isWritable());
        }
        if ("value".equals(id)) { //$NON-NLS-1$
            try {
                Object obj = mbsc.getAttribute(on, attrInfo.getName());
                if (obj instanceof Object[]) {
                    return Arrays.asList((Object[]) obj).toString();
                }
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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
