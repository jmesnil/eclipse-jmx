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
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
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

    private Label attrNameLabel;

    private Label attrTypeLabel;

    private Text attrDescText;

    private Section opSection;

    private MBeanOperationsTable opTable;

    private Section attrDetailsSection;

    private Button attrReadableCheckbox;

    private Button attrWritableCheckbox;

    private Text attrValueText;

    public MBeanInfoView() {
    }

    public void createPartControl(Composite parent) {
        FontData fd[] = parent.getFont().getFontData();
        bold = new Font(parent.getDisplay(), fd[0].getName(), fd[0].height,
                SWT.BOLD);
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.MBeanInfoView_summary);
        form.getBody().setLayout(new TableWrapLayout());

        createInfoSection(form.getBody(), toolkit);
        createAttributesSection(form.getBody(), toolkit);
        createOperationsSection(form.getBody(), toolkit);

        getSite().getPage().addSelectionListener(this);
    }

    private void createInfoSection(Composite parent, FormToolkit toolkit) {
        Composite infoSectionClient = createSection(parent, toolkit,
                Messages.MBeanInfoView_infoSectionTitle,
                Messages.MBeanInfoView_infoSectionDesc, Section.TITLE_BAR
                        | Section.TWISTIE | Section.DESCRIPTION);
        infoSection = (Section) infoSectionClient.getParent();

        toolkit.createLabel(infoSectionClient, Messages.name);
        nameLabel = toolkit.createLabel(infoSectionClient, ""); //$NON-NLS-1$
        nameLabel.setFont(bold);
        nameLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        toolkit.createLabel(infoSectionClient, Messages.MBeanInfoView_javaClass);
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
        // end of info section
    }

    private Composite createAttributesSection(Composite parent,
            FormToolkit toolkit) {
        Composite attrSectionClient = createSection(parent, toolkit,
                Messages.MBeanInfoView_attrSectionTitle,
                Messages.MBeanInfoView_attrSectionDesc, Section.TITLE_BAR
                        | Section.TWISTIE | Section.DESCRIPTION);
        attrSection = (Section) attrSectionClient.getParent();
        attributesTable = new MBeanAttributesTable(attrSectionClient, toolkit,
                this);
        attrSection.setClient(attrSectionClient);

        createAttributeDetailsSection(attrSectionClient, toolkit);
        return attrSectionClient;
    }

    private void createAttributeDetailsSection(Composite parent,
            FormToolkit toolkit) {
        Composite attrDetailsSectionClient = createSection(parent, toolkit,
                Messages.details, null, Section.SHORT_TITLE_BAR
                        | Section.EXPANDED);
        attrDetailsSection = (Section) attrDetailsSectionClient.getParent();

        toolkit.createLabel(attrDetailsSectionClient, Messages.name);
        attrNameLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrNameLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        attrNameLabel.setFont(bold);

        toolkit.createLabel(attrDetailsSectionClient, Messages.value);
        attrValueText = toolkit.createText(attrDetailsSectionClient,
                "", SWT.MULTI //$NON-NLS-1$
                        | SWT.WRAP | SWT.READ_ONLY);
        attrValueText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        attrValueText.setFont(bold);

        toolkit.createLabel(attrDetailsSectionClient, Messages.type);
        attrTypeLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrTypeLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        attrTypeLabel.setFont(bold);
        toolkit.createLabel(attrDetailsSectionClient, Messages.description);
        attrDescText = toolkit.createText(attrDetailsSectionClient,
                "", SWT.MULTI //$NON-NLS-1$
                        | SWT.WRAP | SWT.READ_ONLY);
        attrDescText.setFont(bold);
        attrDescText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        attrReadableCheckbox = toolkit.createButton(attrDetailsSectionClient,
                Messages.readable, SWT.CHECK);
        TableWrapData twdata = new TableWrapData(TableWrapData.FILL_GRAB);
        twdata.colspan = 2;
        attrReadableCheckbox.setLayoutData(twdata);
        attrWritableCheckbox = toolkit.createButton(attrDetailsSectionClient,
                Messages.writable, SWT.CHECK);
        twdata = new TableWrapData(TableWrapData.FILL_GRAB);
        twdata.colspan = 2;
        attrWritableCheckbox.setLayoutData(twdata);

    }

    private Composite createOperationsSection(Composite parent,
            FormToolkit toolkit) {
        Composite opSectionClient = createSection(parent, toolkit,
                Messages.MBeanInfoView_opSectionTitle,
                Messages.MBeanInfoView_opSectionDesct, Section.TITLE_BAR
                        | Section.TWISTIE | Section.DESCRIPTION);
        opSection = (Section) opSectionClient.getParent();
        opTable = new MBeanOperationsTable(opSectionClient, toolkit);
        opSection.setClient(opSectionClient);

        return opSectionClient;
    }

    private Composite createSection(Composite parent, FormToolkit toolkit,
            String title, String description, int flags) {
        Section section = toolkit.createSection(parent, flags);
        section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        if (title != null)
            section.setText(title);
        if (description != null)
            section.setDescription(description);
        section.setEnabled(false);
        Composite sectionClient = toolkit.createComposite(section);
        TableWrapLayout twlayout = new TableWrapLayout();
        twlayout.numColumns = 2;
        sectionClient.setLayout(twlayout);
        section.setClient(sectionClient);
        return sectionClient;
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
        descText.setText(selectedMBeanInfo.getMBeanInfo().getDescription());
    }

    private void updateAttributesArea(boolean forceExpand) {
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

    private void updateDetailsAttributesArea(MBeanAttributeInfoWrapper wrapper) {
        Assert.isNotNull(wrapper);

        attrDetailsSection.setExpanded(true);

        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        attrNameLabel.setText(attrInfo.getName());

        String attrValue = ""; //$NON-NLS-1$
        try {
            MBeanServerConnection mbsc = wrapper.getMBeanServerConnection();
            ObjectName on = wrapper.getObjectName();
            Object obj = mbsc.getAttribute(on, attrInfo.getName());
            attrValue = StringUtils.toString(obj, true);
        } catch (Exception e) {
            e.printStackTrace();
            attrValue = Messages.MBeanAttributesTable_unvailable;
        }
        if (attrValue.equals(Messages.MBeanAttributesTable_unvailable)) {
            attrValueText.setForeground(form.getDisplay().getSystemColor(
                    SWT.COLOR_RED));
        } else {
            attrValueText.setForeground(attrNameLabel.getForeground());
        }
        attrValueText.setText(attrValue);

        attrTypeLabel.setText(StringUtils.toString(attrInfo.getType()));
        attrDescText.setText(attrInfo.getDescription());
        attrReadableCheckbox.setSelection(attrInfo.isReadable());
    }

    public void dispose() {
        bold.dispose();
        form.dispose();
        super.dispose();
    }

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
                attrDetailsSection.setExpanded(false);
                form.reflow(true);
            }
        }
        if (MBeanInfoView.ID.equals(id)) {
            if (obj instanceof MBeanAttributeInfoWrapper) {
                MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) obj;
                if (wrapper.getMBeanInfoWrapper() == selectedMBeanInfo) {
                    updateDetailsAttributesArea(wrapper);
                    form.reflow(true);
                }
            }
        }
    }
}
