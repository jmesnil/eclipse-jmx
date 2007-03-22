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
import net.jmesnil.jmx.ui.JMXUIActivator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class MBeanEditor extends FormEditor {

    public static final String ID = "net.jmesnil.jmx.ui.internal.editors.MBeanEditor"; //$NON-NLS-1$

    public MBeanEditor() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        super.init(site, input);
        MBeanInfoWrapper wrapper = ((MBeanEditorInput) input).getWrapper();
        setPartName(wrapper.getObjectName().getCanonicalName());
    }

    protected FormToolkit createToolkit(Display display) {
        return new FormToolkit(Display.getDefault());
    }

    protected void addPages() {
        try {
            addPage(new AttributesPage(this));
            addPage(new OperationsPage(this));
            // addPage(new NotificationsPage(this));
            addPage(new InfoPage(this));
        } catch (PartInitException e) {
            JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
        }
    }

    public void doSave(IProgressMonitor monitor) {

    }

    public void doSaveAs() {
    }

    public boolean isSaveAsAllowed() {
        return false;
    }
}