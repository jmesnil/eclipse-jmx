package net.jmesnil.jmx.ui.internal.views;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;
import net.jmesnil.jmx.ui.internal.controls.AttributeControlFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributeDetailsSection {

    private Section attrDetailsSection;

    private Label attrNameLabel;

    private Label attrTypeLabel;

    private Text attrDescText;

    private Button attrReadableCheckbox;

    private Button attrWritableCheckbox;

    private MBeanAttributeInfoWrapper selectedWrapper;

    private Composite valueComposite;

    private FormToolkit toolkit;

    private final IWritableAttributeHandler updateAttributeHandler = new IWritableAttributeHandler() {
        public void write(Object newValue) {
            try {
                MBeanServerConnection mbsc = selectedWrapper
                        .getMBeanServerConnection();
                String attrName = selectedWrapper.getMBeanAttributeInfo()
                        .getName();
                Attribute attr = new Attribute(attrName, newValue);
                mbsc.setAttribute(selectedWrapper.getObjectName(), attr);
                attrDetailsSection.layout(true);
                MBeanInfoView mbeanInfoView = (MBeanInfoView) ViewUtil
                        .getView(MBeanInfoView.ID);
                mbeanInfoView.updateAttributesArea(true);
            } catch (Exception e) {
                MessageDialog.openError(attrDetailsSection.getShell(),
                        Messages.AttributeDetailsSection_errorTitle, e
                                .getLocalizedMessage());
            }
        }
    };

    public AttributeDetailsSection(Composite parent, FormToolkit toolkit) {
        this.toolkit = toolkit;

        FontData fd[] = parent.getFont().getFontData();
        Font bold = new Font(parent.getDisplay(), fd[0].getName(),
                fd[0].height, SWT.BOLD);

        Composite attrDetailsSectionClient = ViewUtil.createSection(parent,
                toolkit, Messages.details, null,
                ExpandableComposite.SHORT_TITLE_BAR
                        | ExpandableComposite.EXPANDED);
        attrDetailsSection = (Section) attrDetailsSectionClient.getParent();
        attrDetailsSection.setExpanded(false);

        toolkit.createLabel(attrDetailsSectionClient, Messages.name);
        attrNameLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrNameLabel.setFont(bold);
        attrNameLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        toolkit.createLabel(attrDetailsSectionClient, Messages.value);
        valueComposite = toolkit.createComposite(attrDetailsSectionClient);
        valueComposite.setLayoutData(new TableWrapData(TableWrapData.FILL));
        valueComposite.setLayout(new TableWrapLayout());

        toolkit.createLabel(attrDetailsSectionClient, Messages.type);
        attrTypeLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrTypeLabel.setFont(bold);
        attrTypeLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        toolkit.createLabel(attrDetailsSectionClient, Messages.description);
        attrDescText = toolkit.createText(attrDetailsSectionClient,
                "", SWT.MULTI //$NON-NLS-1$
                        | SWT.WRAP | SWT.READ_ONLY);
        attrDescText.setFont(bold);
        attrDescText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        attrReadableCheckbox = toolkit.createButton(attrDetailsSectionClient,
                Messages.readable, SWT.CHECK);
        attrReadableCheckbox.setEnabled(false);
        attrReadableCheckbox.setFont(bold);
        TableWrapData twdata = new TableWrapData(TableWrapData.FILL_GRAB);
        twdata.colspan = 2;
        attrReadableCheckbox.setLayoutData(twdata);

        attrWritableCheckbox = toolkit.createButton(attrDetailsSectionClient,
                Messages.writable, SWT.CHECK);
        attrWritableCheckbox.setEnabled(false);
        attrWritableCheckbox.setFont(bold);
        twdata = new TableWrapData(TableWrapData.FILL_GRAB);
        twdata.colspan = 2;
        attrWritableCheckbox.setLayoutData(twdata);
    }

    public void update(MBeanAttributeInfoWrapper wrapper) {
        if (wrapper == null) {
            attrDetailsSection.setEnabled(false);
            attrDetailsSection.setExpanded(false);
            return;
        } else {
            attrDetailsSection.setEnabled(true);
            attrDetailsSection.setExpanded(true);
        }

        this.selectedWrapper = wrapper;
        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        boolean writable = attrInfo.isWritable();
        attrNameLabel.setText(attrInfo.getName());
        attrTypeLabel.setText(StringUtils.toString(attrInfo.getType()));
        attrDescText.setText(attrInfo.getDescription());
        attrReadableCheckbox.setSelection(attrInfo.isReadable());
        attrWritableCheckbox.setSelection(writable);

        Object value = null;
        try {
            MBeanServerConnection mbsc = wrapper.getMBeanServerConnection();
            ObjectName on = wrapper.getObjectName();
            value = mbsc.getAttribute(on, attrInfo.getName());
        } catch (Exception e) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
			Messages.MBeanAttributeValue_Warning,
			attrInfo.getName()), e);
        }

        disposeChildren(valueComposite);
        Control attrControl = AttributeControlFactory.createControl(valueComposite, toolkit,
                attrInfo, value, updateAttributeHandler);
        attrControl.pack(true);
        valueComposite.layout(true, true);
    }

    private void disposeChildren(Composite composite) {
        if (composite != null && !composite.isDisposed()) {
            Control[] childs = composite.getChildren();
            if (childs.length > 0) {
                for (int i = 0; i < childs.length; i++) {
                    childs[i].dispose();
                }
            }
        }
    }
}
