/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.editors;

import net.jmesnil.jmx.core.MBeanInfoWrapper;
import net.jmesnil.jmx.core.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.Messages;
import net.jmesnil.jmx.ui.internal.tables.MBeanOperationsTable;

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

public class OperationsSection extends SectionPart {

    private MBeanOperationsTable operationsTable;

    public OperationsSection(MBeanInfoWrapper wrapper,
            final IManagedForm managedForm, Composite parent) {
        super(parent, managedForm.getToolkit(), Section.TITLE_BAR);

        FormToolkit toolkit = managedForm.getToolkit();
        Section section = getSection();
        section.marginWidth = 10;
        section.marginHeight = 5;
        section.setText(Messages.OperationsSection_title);
        Composite container = toolkit.createComposite(section, SWT.WRAP);
        section.setClient(container);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 2;
        layout.marginHeight = 2;
        container.setLayout(layout);

        operationsTable = new MBeanOperationsTable(container, toolkit);
        operationsTable.setInput(wrapper);

        final SectionPart spart = new SectionPart(section);
        managedForm.addPart(spart);
        operationsTable.getViewer().addSelectionChangedListener(
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
        operationsTable.getViewer().refresh();
    }

    @Override
    public boolean setFormInput(Object input) {
        if (input instanceof MBeanOperationInfoWrapper) {
            MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) input;
            ISelection selection = new StructuredSelection(wrapper);
            operationsTable.getViewer().setSelection(selection, true);
            return true;
        }
        return false;
    }
}
