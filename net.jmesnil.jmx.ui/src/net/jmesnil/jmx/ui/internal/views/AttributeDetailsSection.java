package net.jmesnil.jmx.ui.internal.views;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributeDetailsSection {

    private final class ValueSelectionListener implements Listener
  {
    public void handleEvent(Event event) {
        MBeanServerConnection mbsc = selectedWrapper
                .getMBeanServerConnection();
        String attrName = selectedWrapper.getMBeanAttributeInfo()
                .getName();
        String type = selectedWrapper.getMBeanAttributeInfo().getType();
        try {
            Object value = MBeanUtils.getValue(attrValueText.getText(),
                    type);
            Attribute attr = new Attribute(attrName, value);
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
  }

    private Section attrDetailsSection;

    private Label attrNameLabel;

    private Text attrValueText;

    private Label attrTypeLabel;

    private Text attrDescText;

    private Button attrReadableCheckbox;

    private Button attrWritableCheckbox;

    private MBeanAttributeInfoWrapper selectedWrapper;

    private Listener listener;

    private Composite valueComposite;

    private FormToolkit toolkit;

    public AttributeDetailsSection(Composite parent, FormToolkit toolkit) {
        this.toolkit = toolkit;
        listener = new ValueSelectionListener();
      
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
        } else {
            attrDetailsSection.setEnabled(true);
            attrDetailsSection.setExpanded(true);
        }

        this.selectedWrapper = wrapper;
        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        boolean writable = attrInfo.isWritable();
        attrNameLabel.setText(attrInfo.getName());

        if (valueComposite != null && !valueComposite.isDisposed()) {
            Control[] childs = valueComposite.getChildren();
            if (childs.length > 0) {
                for (int i = 0; i < childs.length; i++) {
                    childs[i].dispose();
                }
            }
        }

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
        attrValueText = toolkit.createText(valueComposite,
            "", SWT.SINGLE | SWT.WRAP); //$NON-NLS-1$
        attrValueText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        attrValueText.addListener(SWT.DefaultSelection, listener);

        if (attrValue.equals(Messages.MBeanAttributesTable_unvailable)) {
            attrValueText.setForeground(attrDetailsSection.getDisplay()
                    .getSystemColor(SWT.COLOR_RED));
        } else {
            if (writable) {
                attrValueText.setEditable(true);
                attrValueText.setForeground(attrDetailsSection.getDisplay()
                        .getSystemColor(SWT.COLOR_BLUE));
            } else {
                attrValueText.setEditable(false);
                attrValueText.setForeground(attrNameLabel.getForeground());
            }
        }
        attrValueText.setText(attrValue);

        attrTypeLabel.setText(StringUtils.toString(attrInfo.getType()));
        attrDescText.setText(attrInfo.getDescription());
        attrReadableCheckbox.setSelection(attrInfo.isReadable());
        attrWritableCheckbox.setSelection(writable);
        
        valueComposite.pack(true);
    }

}
