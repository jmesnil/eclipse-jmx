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

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class MBeanInfoView extends ViewPart implements ISelectionListener {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.MBeanInfoView"; //$NON-NLS-1$

    private Font font;

    private ManagedForm managedForm;

    private Section mbeanInfoSection;

    private Label mbeanInfoClassName;

    private Label mbeanInfoObjectName;

    private Label mbeanInfoDescription;

    private MBeanInfoWrapper selectedMBeanInfo;

    private Section operationsSection;

    private MBeanOperationsTable operationsTable;

    private Section attributesSection;

    private MBeanAttributesTable attributesTable;

    private ManagedForm attributesForm;

    public MBeanInfoView() {
    }

    public void createPartControl(Composite parent) {
        FontData fd[] = parent.getFont().getFontData();
        font = new Font(parent.getDisplay(), fd[0].getName(), fd[0].height,
                SWT.BOLD);
        managedForm = new ManagedForm(parent);
        managedForm.getForm().setText(Messages.MBeanInfoView_summary);

        Composite body = managedForm.getForm().getBody();
        body.setLayout(new GridLayout());
        body.setLayoutData(new GridData(GridData.FILL_BOTH));

        createMBeanInfoArea(body);
        createAttributesArea(body);
        createOperationsArea(body);

        getSite().getPage().addSelectionListener(this);
    }

    public void dispose() {
        font.dispose();
        managedForm.dispose();
        if (attributesForm != null)
            attributesForm.dispose();
        super.dispose();
    }

    private void createMBeanInfoArea(Composite parent) {
        Composite comp = ViewUtil.createSection(
                Messages.MBeanInfoView_infoSectionTitle,
                Messages.MBeanInfoView_infoSectionDesc, managedForm, parent, 2,
                false, true);
        FormToolkit toolkit = managedForm.getToolkit();
        mbeanInfoSection = (Section) comp.getParent();

        toolkit.createLabel(comp, Messages.name);
        mbeanInfoObjectName = toolkit.createLabel(comp, ""); //$NON-NLS-1$
        mbeanInfoObjectName.setFont(font);
        mbeanInfoObjectName
                .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        toolkit.createLabel(comp, Messages.MBeanInfoView_javaClass);
        mbeanInfoClassName = toolkit.createLabel(comp, ""); //$NON-NLS-1$
        mbeanInfoClassName.setFont(font);
        mbeanInfoClassName
                .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        toolkit.createLabel(comp, Messages.description);
        mbeanInfoDescription = toolkit.createLabel(comp, ""); //$NON-NLS-1$
        mbeanInfoDescription.setFont(font);
        mbeanInfoDescription.setLayoutData(new GridData(
                GridData.FILL_HORIZONTAL));
    }

    private void updateMBeanInfoArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null;

        mbeanInfoSection.setEnabled(enabled);
        if (!mbeanInfoSection.isEnabled() || forceExpand)
            mbeanInfoSection.setExpanded(enabled);
        if (!enabled)
            return;

        mbeanInfoObjectName.setText(selectedMBeanInfo.getObjectName()
                .getCanonicalName());
        mbeanInfoClassName.setText(selectedMBeanInfo.getMBeanInfo()
                .getClassName());
        mbeanInfoDescription.setText(selectedMBeanInfo.getMBeanInfo()
                .getDescription());
    }

    private void createAttributesArea(Composite parent) {
        Composite comp = ViewUtil.createSection(
                Messages.MBeanInfoView_attrSectionTitle,
                Messages.MBeanInfoView_attrSectionDesc, managedForm, parent, 1,
                false, true);

        attributesSection = (Section) comp.getParent();
        ScrolledForm attrForm = getToolkit().createScrolledForm(comp);
        Composite attrBody = attrForm.getBody();
        GridLayout glayout = new GridLayout(1, true);
        glayout.marginWidth = glayout.marginHeight = 0;
        attrBody.setLayout(glayout);
        attrBody.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        MasterDetailsBlock attrBlock = new MasterDetailsBlock() {

            protected void createMasterPart(final IManagedForm managedForm,
                    Composite parent) {
                Composite c = managedForm.getToolkit().createComposite(parent,
                        SWT.NULL);
                GridLayout glayout = new GridLayout(1, true);
                c.setLayout(glayout);
                attributesTable = new MBeanAttributesTable(c,
                        MBeanInfoView.this);
                final SectionPart spart = new SectionPart(c, managedForm
                        .getToolkit(), SWT.NULL);
                managedForm.addPart(spart);

                attributesTable.getViewer().addSelectionChangedListener(
                        new ISelectionChangedListener() {
                            public void selectionChanged(
                                    SelectionChangedEvent event) {
                                managedForm.fireSelectionChanged(spart, event
                                        .getSelection());
                            }
                        });
            }

            protected void createToolBarActions(IManagedForm managedForm) {
            }

            protected void registerPages(DetailsPart detailsPart) {
                detailsPart.registerPage(MBeanAttributeInfoWrapper.class,
                        new AttributesDetailPage());
            }
        };
        attributesForm = new ManagedForm(getToolkit(), attrForm);
        attrBlock.createContent(attributesForm);

    }

    private void updateAttributesArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null
                && selectedMBeanInfo.getMBeanInfo() != null
                && (selectedMBeanInfo.getMBeanInfo().getAttributes().length > 0);

        attributesSection.setEnabled(enabled);
        if (!attributesSection.isEnabled() || forceExpand)
            attributesSection.setExpanded(enabled);
        if (!enabled)
            return;

        MBeanInfo info = selectedMBeanInfo.getMBeanInfo();
        MBeanAttributeInfo[] attrs = info.getAttributes();
        if (attrs.length > 0)
            attributesTable.setInput(selectedMBeanInfo);
    }

    private void createOperationsArea(Composite parent) {
        Composite comp = ViewUtil.createSection(
                Messages.MBeanInfoView_opSectionTitle,
                Messages.MBeanInfoView_opSectionDesct, managedForm, parent, 1,
                false, true);
        operationsSection = (Section) comp.getParent();
        operationsTable = new MBeanOperationsTable(comp, this);
    }

    private void updateOperationsArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null
                && selectedMBeanInfo.getMBeanInfo() != null
                && (selectedMBeanInfo.getMBeanInfo().getOperations().length > 0);

        operationsSection.setEnabled(enabled);
        if (!operationsSection.isEnabled() || forceExpand)
            operationsSection.setExpanded(enabled);
        if (!enabled)
            return;

        MBeanInfo info = selectedMBeanInfo.getMBeanInfo();
        MBeanOperationInfo[] ops = info.getOperations();
        if (ops.length > 0)
            operationsTable.setInput(selectedMBeanInfo);
    }

    public void setFocus() {
        managedForm.setFocus();
    }

    protected FormToolkit getToolkit() {
        return managedForm.getToolkit();
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (!(selection instanceof IStructuredSelection))
            return;

        Object obj = ((IStructuredSelection) selection).getFirstElement();

        String id = part.getSite().getId();
        if (id.equals(MBeanView.ID)) {
            if (obj instanceof MBeanInfoWrapper) {
                MBeanInfoWrapper wrapper = (MBeanInfoWrapper) obj;
                if (wrapper == selectedMBeanInfo) {
                    return;
                }
                selectedMBeanInfo = wrapper;
                updateMBeanInfoArea(true);
                updateAttributesArea(true);
                updateOperationsArea(true);
            }
        }
    }
}
