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

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import net.jmesnil.jmx.resources.Impact;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MBeanOperationInfoPropertySourceAdapter implements IPropertySource {

    private MBeanOperationInfo opInfo;

    public MBeanOperationInfoPropertySourceAdapter(MBeanOperationInfo opInfo) {
        this.opInfo = opInfo;
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
        addDescriptor(
                "returnType", Messages.returnType, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor("impact", Messages.impact, Messages.general, descriptors); //$NON-NLS-1$
        addDescriptor(
                "writable", Messages.writable, Messages.general, descriptors); //$NON-NLS-1$
        MBeanParameterInfo[] paramInfos = opInfo.getSignature();
        for (int i = 0; i < paramInfos.length; i++) {
            MBeanParameterInfo paramInfo = paramInfos[i];
            addDescriptor(
                    "param" + i, paramInfo.getName(), Messages.parameters, //$NON-NLS-1$
                    descriptors);
        }
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
            return opInfo.getName();
        }
        if ("description".equals(id)) { //$NON-NLS-1$
            return opInfo.getDescription();
        }
        if ("returnType".equals(id)) { //$NON-NLS-1$
            return opInfo.getReturnType();
        }
        if ("impact".equals(id)) { //$NON-NLS-1$
            return Impact.parseInt(opInfo.getImpact());
        }
        if (id instanceof String) {
            String idStr = (String) id;
            if (idStr.startsWith("param")) { //$NON-NLS-1$
                String indexStr = idStr.substring(idStr.length() - 1);
                int i = new Integer(indexStr).intValue();
                for (int j = 0; j < opInfo.getSignature().length; j++) {
                    MBeanParameterInfo paramInfo = opInfo.getSignature()[j];
                    if (i == j) {
                        return paramInfo.getType();
                    }
                }
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
