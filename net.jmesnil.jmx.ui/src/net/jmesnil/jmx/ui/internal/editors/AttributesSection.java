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

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.tables.MBeanAttributesTable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class AttributesSection extends SectionPart {

    private MBeanAttributesTable attributesTable;

    public AttributesSection(MBeanInfoWrapper wrapper,
            final IManagedForm managedForm, Composite parent) {
        super(parent, managedForm.getToolkit(), Section.TITLE_BAR);

        FormToolkit toolkit = managedForm.getToolkit();
        Section section = getSection();
        section.marginWidth = 10;
        section.marginHeight = 5;
        section.setText(Messages.AttributesSection_title);
        Composite container = toolkit.createComposite(section, SWT.WRAP);
        section.setClient(container);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 2;
        layout.marginHeight = 2;
        container.setLayout(layout);

        attributesTable = new MBeanAttributesTable(container, toolkit);
        attributesTable.setInput(wrapper);

        final SectionPart spart = new SectionPart(section);
        managedForm.addPart(spart);
        attributesTable.getViewer().addSelectionChangedListener(
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        managedForm.fireSelectionChanged(spart, event
                                .getSelection());
                    }
                });
    }

    @Override
    public void refresh() {
        super.refresh();
        attributesTable.getViewer().refresh();
    }
    
    @Override
    public boolean setFormInput(Object input) {
        if (input instanceof MBeanAttributeInfoWrapper) {
            MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) input;
            ISelection selection = new StructuredSelection(wrapper);
            attributesTable.getViewer().setSelection(selection, true);
            return true;
        }
        return false;
    }
}
