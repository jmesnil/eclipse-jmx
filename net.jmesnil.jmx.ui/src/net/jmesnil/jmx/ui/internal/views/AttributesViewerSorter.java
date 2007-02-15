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

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

class AttributesViewerSorter extends ViewerSorter {
    int direction, index;

    protected AttributesViewerSorter(int direction, int index) {
        this.direction = (direction == SWT.UP ? -1 : 1);
        this.index = index;
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        if (e1 instanceof MBeanAttributeInfoWrapper
                && e2 instanceof MBeanAttributeInfoWrapper) {
            MBeanAttributeInfo attrInfo1 = ((MBeanAttributeInfoWrapper) e1)
                    .getMBeanAttributeInfo();
            MBeanAttributeInfo attrInfo2 = ((MBeanAttributeInfoWrapper) e2)
                    .getMBeanAttributeInfo();
            if (index == 0)
                return direction
                        * attrInfo1.getName().compareTo(attrInfo2.getName());

        }
        return direction * super.compare(viewer, e1, e2);
    }
}