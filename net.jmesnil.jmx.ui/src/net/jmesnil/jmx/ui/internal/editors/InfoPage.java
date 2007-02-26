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
import net.jmesnil.jmx.resources.MBeanNotificationInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class InfoPage extends FormPage {

    static final String ID = "info"; //$NON-NLS-1$

    private MBeanInfoWrapper wrapper;

    private Font bold;

    public InfoPage(FormEditor editor) {
        super(editor, ID, Messages.InfoPage_title);
        MBeanEditorInput input = (MBeanEditorInput) editor.getEditorInput();
        this.wrapper = input.getWrapper();
    }

    protected void createFormContent(IManagedForm managedForm) {
        ScrolledForm form = managedForm.getForm();
        form.setText(wrapper.getObjectName().getCanonicalName());
        FormToolkit toolkit = managedForm.getToolkit();
        form.getForm().setSeparatorVisible(true);

        Composite body = form.getBody();
        FontData fd[] = body.getFont().getFontData();
        bold = new Font(body.getDisplay(), fd[0].getName(), fd[0].getHeight(),
                SWT.BOLD);

        GridLayout layout = new GridLayout(2, false);
        body.setLayout(layout);
        GridDataFactory defaultGridData = GridDataFactory.fillDefaults();

        String className = wrapper.getMBeanInfo().getClassName();

        toolkit.createLabel(body, Messages.className);
        Label classNameLabel = toolkit.createLabel(body, className, SWT.WRAP
                | SWT.READ_ONLY);
        classNameLabel.setFont(bold);
        classNameLabel.setLayoutData(defaultGridData.create());

        String description = wrapper.getMBeanInfo().getDescription();

        toolkit.createLabel(body, Messages.description);
        Text descriptionText = toolkit.createText(body, description, SWT.MULTI
                | SWT.WRAP | SWT.READ_ONLY);
        descriptionText.setLayoutData(defaultGridData.create());

        Section notifSection = toolkit.createSection(body, Section.TITLE_BAR
                | Section.TWISTIE | Section.TWISTIE);
        notifSection.setText(Messages.InfoPage_notificationsSectionTitle);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, true).applyTo(
                notifSection);
        if (wrapper.getMBeanNotificationInfoWrappers().length == 0) {
            notifSection.setEnabled(false);
            notifSection.setExpanded(false);
        } else {
            notifSection.setEnabled(true);
            notifSection.setExpanded(true);
        }

        Composite notificationContainer = toolkit.createComposite(notifSection);
        notifSection.setClient(notificationContainer);
        GridLayoutFactory.fillDefaults().generateLayout(notificationContainer);

        Tree notificationTree = toolkit.createTree(notificationContainer,
                SWT.BORDER);
        GridDataFactory.fillDefaults().hint(500, 150).applyTo(notificationTree);

        TreeViewer notificationViewer = new TreeViewer(notificationTree);
        notificationViewer.setLabelProvider(new LabelProvider() {
            public String getText(Object element) {
                if (element instanceof MBeanNotificationInfoWrapper) {
                    MBeanNotificationInfoWrapper notifWrapper = (MBeanNotificationInfoWrapper) element;
                    return notifWrapper.getMBeanNotificationInfo().getName();
                }
                return super.getText(element);
            }
        });
        notificationViewer.setContentProvider(new ITreeContentProvider() {
            public Object[] getChildren(Object parent) {
                if (parent instanceof MBeanNotificationInfoWrapper) {
                    MBeanNotificationInfoWrapper notifWrapper = (MBeanNotificationInfoWrapper) parent;
                    return notifWrapper.getMBeanNotificationInfo()
                            .getNotifTypes();
                }
                return new Object[0];
            }

            public Object getParent(Object element) {
                return null;
            }

            public boolean hasChildren(Object element) {
                if (element instanceof MBeanNotificationInfoWrapper) {
                    MBeanNotificationInfoWrapper notifWrapper = (MBeanNotificationInfoWrapper) element;
                    return (notifWrapper.getMBeanNotificationInfo()
                            .getNotifTypes().length > 0);
                }
                return false;
            }

            public Object[] getElements(Object input) {
                return ((MBeanInfoWrapper) input)
                        .getMBeanNotificationInfoWrappers();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput,
                    Object newInput) {
            }
        });
        notificationViewer.setInput(wrapper);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bold != null) {
            bold.dispose();
        }
    }
}