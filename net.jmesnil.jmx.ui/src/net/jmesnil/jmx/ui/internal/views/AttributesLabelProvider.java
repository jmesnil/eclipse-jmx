/**
 * net.jmesnil.jmx.ui
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
package net.jmesnil.jmx.ui.internal.views;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class AttributesLabelProvider extends LabelProvider implements
        ITableLabelProvider {
    public String getColumnText(Object element, int columnIndex) {
        if (!(element instanceof MBeanAttributeInfoWrapper))
            return super.getText(element);

        MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) element;
        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        switch (columnIndex) {
        case 0:
            return attrInfo.getName();
        case 1:
            try {
                MBeanServerConnection mbsc = wrapper
                        .getMBeanServerConnection();
                ObjectName on = wrapper.getObjectName();
                Object obj = mbsc.getAttribute(on, attrInfo.getName());
                return StringUtils.toString(obj, false);
            } catch (Exception e) {
                e.printStackTrace();
                return Messages.MBeanAttributesTable_unvailable;
            }
        }
        return getText(element);
    }

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }
}