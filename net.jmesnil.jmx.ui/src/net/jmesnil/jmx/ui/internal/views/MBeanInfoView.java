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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class MBeanInfoView extends ViewPart implements ISelectionListener {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.MBeanInfoView"; //$NON-NLS-1$

    private Font bold;

    private MBeanInfoWrapper selectedMBeanInfo;

    private Label nameLabel, javaClassLabel;

    private Section infoSection;

    private Text descText;

    private Section attrSection;

    private MBeanAttributesTable attributesTable;

    private ScrolledForm form;

    private Section opSection;

    private MBeanOperationsTable opTable;

    private ExpansionAdapter expansionAdapter;

    private AttributeDetailsSection attrDetails;

    public MBeanInfoView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        FontData fd[] = parent.getFont().getFontData();
        bold = new Font(parent.getDisplay(), fd[0].getName(),
                fd[0].getHeight(), SWT.BOLD);
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.MBeanInfoView_summary);
        form.getBody().setLayout(new TableWrapLayout());

        expansionAdapter = new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        };

        createInfoSection(form.getBody(), toolkit);
        createAttributesSection(form.getBody(), toolkit);
        createOperationsSection(form.getBody(), toolkit);

        getSite().getPage().addSelectionListener(this);
    }

    private void createInfoSection(Composite parent, FormToolkit toolkit) {
        Composite infoSectionClient = ViewUtil.createSection(parent, toolkit,
                Messages.MBeanInfoView_infoSectionTitle,
                Messages.MBeanInfoView_infoSectionDesc,
                ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                        | Section.DESCRIPTION);
        infoSection = (Section) infoSectionClient.getParent();
        infoSection.addExpansionListener(expansionAdapter);

        toolkit.createLabel(infoSectionClient, Messages.name);
        nameLabel = toolkit.createLabel(infoSectionClient, ""); //$NON-NLS-1$
        nameLabel.setFont(bold);
        nameLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        toolkit
                .createLabel(infoSectionClient,
                        Messages.MBeanInfoView_javaClass);
        javaClassLabel = toolkit.createLabel(infoSectionClient, ""); //$NON-NLS-1$
        javaClassLabel.setFont(bold);
        javaClassLabel
                .setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        toolkit.createLabel(infoSectionClient, Messages.description);
        descText = toolkit.createText(infoSectionClient,
                "", SWT.MULTI | SWT.WRAP //$NON-NLS-1$
                        | SWT.READ_ONLY);
        descText.setFont(bold);
        descText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
    }

    private Composite createAttributesSection(Composite parent,
            FormToolkit toolkit) {
        Composite attrSectionClient = ViewUtil.createSection(parent, toolkit,
                Messages.MBeanInfoView_attrSectionTitle,
                Messages.MBeanInfoView_attrSectionDesc,
                ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                        | Section.DESCRIPTION);
        attrSection = (Section) attrSectionClient.getParent();
        attrSection.addExpansionListener(expansionAdapter);
        attributesTable = new MBeanAttributesTable(attrSectionClient, toolkit,
                this);
        attrSection.setClient(attrSectionClient);

        attrDetails = new AttributeDetailsSection(attrSectionClient, toolkit);

        return attrSectionClient;
    }

    private Composite createOperationsSection(Composite parent,
            FormToolkit toolkit) {
        Composite opSectionClient = ViewUtil.createSection(parent, toolkit,
                Messages.MBeanInfoView_opSectionTitle,
                Messages.MBeanInfoView_opSectionDesct,
                ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                        | Section.DESCRIPTION);
        opSection = (Section) opSectionClient.getParent();
        opSection.addExpansionListener(expansionAdapter);

        opTable = new MBeanOperationsTable(opSectionClient, toolkit);
        opSection.setClient(opSectionClient);

        return opSectionClient;
    }

    private void updateMBeanInfoArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null;

        infoSection.setEnabled(enabled);
        if (!infoSection.isEnabled() || forceExpand)
            infoSection.setExpanded(enabled);
        if (!enabled)
            return;

        nameLabel.setText(selectedMBeanInfo.getObjectName().getCanonicalName());
        javaClassLabel.setText(selectedMBeanInfo.getMBeanInfo().getClassName());
        String description = selectedMBeanInfo.getMBeanInfo().getDescription();
        if (description != null) {
            descText.setText(description);
        }
    }

    void updateAttributesArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null
                && selectedMBeanInfo.getMBeanInfo() != null
                && (selectedMBeanInfo.getMBeanInfo().getAttributes().length > 0);

        attrSection.setEnabled(enabled);
        if (!attrSection.isEnabled() || forceExpand)
            attrSection.setExpanded(enabled);
        if (!enabled)
            return;

        MBeanInfo info = selectedMBeanInfo.getMBeanInfo();
        MBeanAttributeInfo[] attrs = info.getAttributes();
        if (attrs.length > 0)
            attributesTable.setInput(selectedMBeanInfo);
    }

    private void updateOperationsArea(boolean forceExpand) {
        boolean enabled = selectedMBeanInfo != null
                && selectedMBeanInfo.getMBeanInfo() != null
                && (selectedMBeanInfo.getMBeanInfo().getOperations().length > 0);

        opSection.setEnabled(enabled);
        if (!opSection.isEnabled() || forceExpand)
            opSection.setExpanded(enabled);
        if (!enabled)
            return;

        MBeanInfo info = selectedMBeanInfo.getMBeanInfo();
        MBeanOperationInfo[] operations = info.getOperations();
        if (operations.length > 0)
            opTable.setInput(selectedMBeanInfo);
    }

    @Override
    public void dispose() {
        bold.dispose();
        form.dispose();
        super.dispose();
    }

    @Override
    public void setFocus() {
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (!(selection instanceof IStructuredSelection))
            return;

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        String id = part.getSite().getId();
        if (MBeanView.ID.equals(id)) {
            if (obj instanceof MBeanInfoWrapper) {
                MBeanInfoWrapper wrapper = (MBeanInfoWrapper) obj;
                if (wrapper == selectedMBeanInfo) {
                    return;
                }
                selectedMBeanInfo = wrapper;
                updateMBeanInfoArea(true);
                updateAttributesArea(true);
                updateOperationsArea(true);
                attrDetails.update(null);
                form.reflow(true);
            }
        }
        if (MBeanInfoView.ID.equals(id)) {
            if (obj instanceof MBeanAttributeInfoWrapper) {
                MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) obj;
                if (wrapper.getMBeanInfoWrapper() == selectedMBeanInfo) {
                    attrDetails.update(wrapper);
                    form.reflow(true);
                }
            }
        }
    }
}
