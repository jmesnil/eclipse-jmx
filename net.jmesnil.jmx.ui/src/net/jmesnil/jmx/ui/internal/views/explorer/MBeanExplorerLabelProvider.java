/**
 * Eclipse JMX Console
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
package net.jmesnil.jmx.ui.internal.views.explorer;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.tree.DomainNode;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.tree.PropertyNode;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class MBeanExplorerLabelProvider extends LabelProvider {

    private boolean flatLayout;

    public MBeanExplorerLabelProvider() {
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public String getText(Object obj) {
        if (obj instanceof DomainNode) {
            DomainNode node = (DomainNode) obj;
            return node.getDomain();
        }
        if (obj instanceof ObjectNameNode) {
            ObjectNameNode node = (ObjectNameNode) obj;
            if (flatLayout) {
                return node.getObjectName().getKeyPropertyListString();
            } else {
                return node.getValue();
            }
        }
        if (obj instanceof PropertyNode) {
            PropertyNode node = (PropertyNode) obj;
            return node.getValue();
        }
        if (obj instanceof MBeanInfoWrapper) {
            MBeanInfoWrapper wrapper = (MBeanInfoWrapper) obj;
            return wrapper.getObjectName().toString();
        }
        if (obj instanceof MBeanOperationInfoWrapper) {
            MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) obj;
            return MBeanUtils.prettySignature(wrapper.getMBeanOperationInfo());
        }
        if (obj instanceof MBeanAttributeInfoWrapper) {
            MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) obj;
            return wrapper.getMBeanAttributeInfo().getName();
        }
        return obj.toString();
    }

    @Override
    public Image getImage(Object obj) {
        if (obj instanceof DomainNode) {
            return JMXImages.get(JMXImages.IMG_OBJS_LIBRARY);
        }
        if (obj instanceof ObjectNameNode) {
            return JMXImages.get(JMXImages.IMG_OBJS_METHOD);
        }
        if (obj instanceof PropertyNode) {
            return JMXImages.get(JMXImages.IMG_OBJS_PACKAGE);
        }
        if (obj instanceof MBeanInfoWrapper) {
            return JMXImages.get(JMXImages.IMG_OBJS_METHOD);
        }
        if (obj instanceof MBeanAttributeInfoWrapper) {
            return JMXImages.get(JMXImages.IMG_FIELD_PUBLIC);
        }
        if (obj instanceof MBeanOperationInfoWrapper) {
            return JMXImages.get(JMXImages.IMG_MISC_PUBLIC);
        }
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }

    public void setFlatLayout(boolean state) {
        flatLayout = state;
    }
}