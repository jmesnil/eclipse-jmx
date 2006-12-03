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
 * 
 *  Code was inspired from org.eclipse.equinox.client source, (c) 2006 IBM 
 */
package net.jmesnil.jmx.ui.internal.views;

import java.io.IOException;
import java.util.Hashtable;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.DomainWrapper;
import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.resources.MBeanServerConnectionWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MBeanView extends ViewPart {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.MBeanView"; //$NON-NLS-1$

    private TreeViewer viewer;

    protected class ViewContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (parent instanceof MBeanServerConnectionWrapper) {
                MBeanServerConnectionWrapper mbscWrapper = (MBeanServerConnectionWrapper) parent;
                return mbscWrapper.getDomains();
            }
            if (parent instanceof DomainWrapper) {
                DomainWrapper domain = (DomainWrapper) parent;
                return domain.getMBeanInfos();
            }
            if (parent instanceof MBeanInfoWrapper) {
                MBeanInfoWrapper mbeanInfo = (MBeanInfoWrapper) parent;
                return mbeanInfo.getMBeanFeatureInfos();
            }
            return new Object[0];
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof MBeanServerConnectionWrapper) {
                MBeanServerConnectionWrapper wrapper = (MBeanServerConnectionWrapper) parent;
                try {
                    return (wrapper.getMBeanServerConnection().getMBeanCount()
                            .intValue() > 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (parent instanceof MBeanInfoWrapper) {
                MBeanInfoWrapper mbeanInfo = (MBeanInfoWrapper) parent;
                return (mbeanInfo.getMBeanFeatureInfos().length > 0);
            }
            if (parent instanceof MBeanFeatureInfoWrapper) {
                return false;
            }
            return true;
        }
    }

    protected class ViewLabelProvider extends LabelProvider {

        public String getText(Object obj) {
            if (obj instanceof MBeanOperationInfoWrapper) {
                MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) obj;
                return wrapper.getSignature();
            }
            if (obj instanceof MBeanAttributeInfoWrapper) {
                MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) obj;
                MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
                return attrInfo.getName();
            }
            if (obj instanceof MBeanInfoWrapper) {
                MBeanInfoWrapper instance = (MBeanInfoWrapper) obj;
                ObjectName on = instance.getObjectName();
                Hashtable props = on.getKeyPropertyList();
                if (props.size() == 1 && props.containsKey("type")) { //$NON-NLS-1$
                    return (String) props.get("type"); //$NON-NLS-1$
                }
                return on.getCanonicalKeyPropertyListString();
            }
            if (obj instanceof DomainWrapper) {
                DomainWrapper domain = (DomainWrapper) obj;
                return domain.getName();
            }
            if (obj instanceof MBeanServerConnection) {
                MBeanServerConnection connection = (MBeanServerConnection) obj;
                return connection.toString();
            }
            return obj.toString();
        }

        public Image getImage(Object obj) {
            if (obj instanceof MBeanAttributeInfoWrapper) {
                return JMXImages.get(JMXImages.IMG_FIELD_PUBLIC);
            }
            if (obj instanceof MBeanOperationInfoWrapper) {
                return JMXImages.get(JMXImages.IMG_MISC_PUBLIC);
            }
            if (obj instanceof MBeanInfoWrapper) {
                return JMXImages.get(JMXImages.IMG_OBJS_INTERFACE);
            }
            if (obj instanceof DomainWrapper) {
                return JMXImages.get(JMXImages.IMG_OBJS_LIBRARY);
            }
            String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
            return PlatformUI.getWorkbench().getSharedImages().getImage(
                    imageKey);
        }
    }

    /**
     * The constructor.
     */
    public MBeanView() {
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        getViewSite().setSelectionProvider(viewer);
    }

    public void setMBeanServerConnection(MBeanServerConnectionWrapper connection) {
        viewer.setInput(connection);
        viewer.refresh();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}