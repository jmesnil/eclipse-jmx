/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

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