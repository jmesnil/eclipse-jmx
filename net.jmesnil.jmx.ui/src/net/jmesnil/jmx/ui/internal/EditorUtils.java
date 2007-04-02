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
package net.jmesnil.jmx.ui.internal;

import net.jmesnil.jmx.resources.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditor;
import net.jmesnil.jmx.ui.internal.editors.MBeanEditorInput;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class EditorUtils {

    public static IEditorInput getEditorInput(Object input) {
        Assert.isNotNull(input);
        
        if (input instanceof ObjectNameNode) {
            ObjectNameNode node = (ObjectNameNode) input;
            MBeanInfoWrapper wrapper = node.getMbeanInfoWrapper();
            return new MBeanEditorInput(wrapper);
        }
        if (input instanceof MBeanInfoWrapper) {
            MBeanInfoWrapper wrapper = (MBeanInfoWrapper) input;
            return new MBeanEditorInput(wrapper);
        }
        if (input instanceof MBeanFeatureInfoWrapper) {
            MBeanFeatureInfoWrapper wrapper = (MBeanFeatureInfoWrapper) input;
            return new MBeanEditorInput(wrapper.getMBeanInfoWrapper());
        }

        return null;
    }

    public static IEditorPart isOpenInEditor(Object inputElement) {
        IEditorInput input = getEditorInput(inputElement);
        return isOpenInEditor(input);
    }

    private static IEditorPart isOpenInEditor(IEditorInput input) {
        if (input != null) {
            IWorkbenchPage p = JMXUIActivator.getActivePage();
            if (p != null) {
                return p.findEditor(input);
            }
        }
        return null;
    }

    public static IEditorPart openMBeanEditor(IEditorInput input) {
        IEditorPart part = EditorUtils.isOpenInEditor(input);
        if (part != null) {
            JMXUIActivator.getActivePage().bringToTop(part);
            return part;
        } else {
            try {
                return JMXUIActivator.getActivePage().openEditor(input,
                        MBeanEditor.ID);
            } catch (PartInitException e) {
                JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
            }
        }
        return null;
    }

    public static void revealInEditor(IEditorPart editor, Object input) {
        if (input instanceof MBeanFeatureInfoWrapper) {
            MBeanFeatureInfoWrapper feature = (MBeanFeatureInfoWrapper) input;
            if (editor instanceof MBeanEditor) {
                ((MBeanEditor) editor).selectReveal(feature);
            }
        }
    }
}