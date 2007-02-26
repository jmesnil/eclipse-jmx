/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
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
package net.jmesnil.jmx.ui.internal.editors;

import net.jmesnil.jmx.resources.MBeanInfoWrapper;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class MBeanEditorInput implements IEditorInput {

    private MBeanInfoWrapper wrapper;

    public MBeanEditorInput(MBeanInfoWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public boolean exists() {
        return false;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return wrapper.getObjectName().toString();
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return wrapper.getObjectName().getCanonicalName();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public Object getAdapter(Class adapter) {
        return null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MBeanEditorInput)) {
            return false;
        }
        MBeanEditorInput other = (MBeanEditorInput) obj;
        return other.wrapper.getObjectName().equals(wrapper.getObjectName());
    }

    public MBeanInfoWrapper getWrapper() {
        return wrapper;
    }

}
