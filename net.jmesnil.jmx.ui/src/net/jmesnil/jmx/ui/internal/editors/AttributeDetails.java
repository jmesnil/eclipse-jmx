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

import java.io.PrintWriter;
import java.io.StringWriter;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributeDetails extends AbstractFormPart implements IDetailsPage {

    private IFormPart masterSection;

    private FormToolkit toolkit;

    private Label nameLabel;

    private Label typeLabel;

    private Text descriptionText;

    private Label permissionLabel;

    private Composite valueComposite;

    private MBeanAttributeInfoWrapper wrapper;

    private final IWritableAttributeHandler updateAttributeHandler = new IWritableAttributeHandler() {
        public void write(Object newValue) {
            try {
                MBeanServerConnection mbsc = wrapper.getMBeanServerConnection();
                String attrName = wrapper.getMBeanAttributeInfo().getName();
                Attribute attr = new Attribute(attrName, newValue);
                mbsc.setAttribute(wrapper.getObjectName(), attr);
                masterSection.refresh();
            } catch (Exception e) {
                MessageDialog.openError(getManagedForm().getForm().getDisplay()
                        .getActiveShell(),
                        Messages.AttributeDetailsSection_errorTitle, e
                                .getLocalizedMessage());
            }
        }
    };

    public AttributeDetails(IFormPart masterSection) {
        this.masterSection = masterSection;
    }

    public void createContents(Composite parent) {
        TableWrapLayout layout = new TableWrapLayout();
        layout.topMargin = 5;
        layout.leftMargin = 5;
        layout.rightMargin = 2;
        layout.bottomMargin = 2;
        parent.setLayout(layout);

        toolkit = getManagedForm().getToolkit();

        FontData fd[] = parent.getFont().getFontData();
        Font bold = new Font(parent.getDisplay(), fd[0].getName(), fd[0]
                .getHeight(), SWT.BOLD);

        Section section = toolkit.createSection(parent, Section.TITLE_BAR);
        section.marginWidth = 10;
        section.setText(Messages.AttributeDetails_title);
        section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        Composite container = toolkit.createComposite(section);
        section.setClient(container);
        GridLayout glayout = new GridLayout();
        glayout.marginWidth = glayout.marginHeight = 0;
        glayout.numColumns = 2;
        glayout.makeColumnsEqualWidth = false;
        container.setLayout(glayout);

        toolkit.createLabel(container, Messages.name);
        nameLabel = toolkit.createLabel(container, ""); //$NON-NLS-1$
        nameLabel.setFont(bold);
        nameLabel.setLayoutData(newLayoutData());

        toolkit.createLabel(container, Messages.type);
        typeLabel = toolkit.createLabel(container, ""); //$NON-NLS-1$
        typeLabel.setFont(bold);
        typeLabel.setLayoutData(newLayoutData());

        toolkit.createLabel(container, Messages.description);
        descriptionText = toolkit.createText(container, "", SWT.MULTI //$NON-NLS-1$
                | SWT.WRAP | SWT.READ_ONLY);
        descriptionText.setFont(bold);
        descriptionText.setLayoutData(newLayoutData());

        toolkit.createLabel(container, Messages.permission);
        permissionLabel = toolkit.createLabel(container, ""); //$NON-NLS-1$
        permissionLabel.setLayoutData(newLayoutData());
        permissionLabel.setFont(bold);

        valueComposite = toolkit.createComposite(container);
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        layoutData.horizontalSpan = 2;
        valueComposite.setLayoutData(layoutData);
        valueComposite.setLayout(new GridLayout());
    }

    private GridData newLayoutData() {
        return new GridData(SWT.FILL, SWT.FILL, false, false);
    }

    public void selectionChanged(IFormPart part, ISelection selection) {
        StructuredSelection structured = (StructuredSelection) selection;
        this.wrapper = (MBeanAttributeInfoWrapper) structured.getFirstElement();
        update(wrapper);
    }

    public void update(MBeanAttributeInfoWrapper wrapper) {
        MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
        String type = attrInfo.getType();
        nameLabel.setText(attrInfo.getName());
        typeLabel.setText(StringUtils.toString(type));
        descriptionText.setText(attrInfo.getDescription());

        boolean writable = attrInfo.isWritable();
        boolean readable = attrInfo.isReadable();

        if (readable && writable) {
            permissionLabel.setImage(JMXImages
                    .get(JMXImages.IMG_OBJS_READ_WRITE));
            permissionLabel.setToolTipText(Messages.readWrite);
        } else if (readable && !writable) {
            permissionLabel.setImage(JMXImages.get(JMXImages.IMG_OBJS_READ));
            permissionLabel.setToolTipText(Messages.readOnly);
        } else if (writable && !readable) {
            permissionLabel.setImage(JMXImages.get(JMXImages.IMG_OBJS_WRITE));
            permissionLabel.setToolTipText(Messages.writeOnly);
        } else {
            permissionLabel.setImage(null);
        }

        disposeChildren(valueComposite);

        try {
            Control attrControl = AttributeControlFactory.createControl(
                    valueComposite, toolkit, writable, type,
                    wrapper.getValue(), updateAttributeHandler);
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            attrControl.setLayoutData(gd);
            attrControl.pack(true);
        } catch (Throwable t) {
            JMXUIActivator.log(IStatus.ERROR, NLS.bind(
                    Messages.MBeanAttributeValue_Warning, attrInfo.getName()),
                    t);
            Label errorLabel = toolkit.createLabel(valueComposite,
                    Messages.unavailable);
            errorLabel.setForeground(valueComposite.getDisplay()
                    .getSystemColor(SWT.COLOR_RED));
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            errorLabel.setLayoutData(gd);
            Text errorText = toolkit.createText(valueComposite, "", //$NON-NLS-1$
                    SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
            gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            errorText.setLayoutData(gd);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            errorText.setText(sw.toString());
        } finally {
            valueComposite.layout(true, true);
        }
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