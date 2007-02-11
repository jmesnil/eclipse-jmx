package net.jmesnil.jmx.ui.internal.views;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;
import net.jmesnil.jmx.ui.internal.controls.AttributeControlFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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

    private Label attrPermissionLabel;

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
        Font bold = new Font(parent.getDisplay(), fd[0].getName(), fd[0]
                .getHeight(), SWT.BOLD);

        Composite attrDetailsSectionClient = ViewUtil.createSection(parent,
                toolkit, Messages.details, null,
                ExpandableComposite.SHORT_TITLE_BAR
                        | ExpandableComposite.EXPANDED);
        attrDetailsSection = (Section) attrDetailsSectionClient.getParent();
        attrDetailsSection.setExpanded(false);

        toolkit.createLabel(attrDetailsSectionClient, Messages.name);
        attrNameLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrNameLabel.setFont(bold);
        attrNameLabel.setLayoutData(newLayoutData());

        toolkit.createLabel(attrDetailsSectionClient, Messages.type);
        attrTypeLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrTypeLabel.setFont(bold);
        attrTypeLabel.setLayoutData(newLayoutData());

        toolkit.createLabel(attrDetailsSectionClient, Messages.description);
        attrDescText = toolkit.createText(attrDetailsSectionClient,
                "", SWT.MULTI //$NON-NLS-1$
                        | SWT.WRAP | SWT.READ_ONLY);
        attrDescText.setFont(bold);
        attrDescText.setLayoutData(newLayoutData());

        toolkit.createLabel(attrDetailsSectionClient, Messages.AttributeDetailsSection_permission);
        attrPermissionLabel = toolkit.createLabel(attrDetailsSectionClient, ""); //$NON-NLS-1$
        attrPermissionLabel.setLayoutData(newLayoutData());
        attrPermissionLabel.setFont(bold);

        toolkit.createLabel(attrDetailsSectionClient, Messages.value);
        valueComposite = toolkit.createComposite(attrDetailsSectionClient);
        valueComposite.setLayoutData(newLayoutData());
        valueComposite.setLayout(new TableWrapLayout());
    }

    private TableWrapData newLayoutData() {
        return new TableWrapData(TableWrapData.FILL_GRAB);
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
        String type = attrInfo.getType();
        attrNameLabel.setText(attrInfo.getName());
        attrTypeLabel.setText(StringUtils.toString(type));
        attrDescText.setText(attrInfo.getDescription());

        boolean writable = attrInfo.isWritable();
        boolean readable = attrInfo.isReadable();

        if (readable && writable) {
            attrPermissionLabel.setImage(JMXImages.get(JMXImages.IMG_OBJS_READ_WRITE));
            attrPermissionLabel.setToolTipText(Messages.readWrite);
        } else if (readable && !writable) {
            attrPermissionLabel.setImage(JMXImages.get(JMXImages.IMG_OBJS_READ));
            attrPermissionLabel.setToolTipText(Messages.readOnly);
        } else if (writable && !readable){
            attrPermissionLabel.setImage(JMXImages.get(JMXImages.IMG_OBJS_WRITE));
            attrPermissionLabel.setToolTipText(Messages.writeOnly);
        } else {
            attrPermissionLabel.setImage(null);
        }
        
        disposeChildren(valueComposite);
        
        Control attrControl;
        try {
            attrControl = AttributeControlFactory.createControl(
                    valueComposite, toolkit, writable, type, wrapper.getValue(), updateAttributeHandler);
        } catch (Throwable t) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
                    Messages.MBeanAttributeValue_Warning, attrInfo.getName()),
                    t);
            // FIXME should not be presented as a regular attribute but should be displayed
            // with a dedicated control (similar to ErrorDialog control)
            attrControl = AttributeControlFactory.createControl(
                    valueComposite, toolkit, false, t.getClass().getName(), t, updateAttributeHandler);
        }
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
