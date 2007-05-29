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

import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationListener;

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class NotificationsPage extends FormPage {

    private final class NotificationLabelProvider extends LabelProvider
            implements ITableLabelProvider {
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            Notification notification = (Notification) element;
            if (columnIndex == 0) {
                return Long.toString(notification.getTimeStamp());
            }
            if (columnIndex == 1) {
                return notification.getType();
            }
            if (columnIndex == 2) {
                return notification.getMessage();
            }
            if (columnIndex == 3) {
                if (notification.getUserData() != null)
                    return notification.getUserData().toString();
                else
                    return ""; //$NON-NLS-1$
            }
            if (columnIndex == 4) {
                return Long.toString(notification.getSequenceNumber());
            }
            if (columnIndex == 5) {
                return notification.getSource().toString();
            }
            return super.getText(element);
        }
    }

    static final String ID = "notifications"; //$NON-NLS-1$

    private MBeanInfoWrapper wrapper;

    private Action subscribeAction;

    private NotificationListener listener;

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private List notifications = new ArrayList();

    private TableViewer viewer;

    public NotificationsPage(FormEditor editor) {
        super(editor, ID, Messages.NotificationsPage_title);
        MBeanEditorInput input = (MBeanEditorInput) editor.getEditorInput();
        this.wrapper = input.getWrapper();
    }

    private void createToolBarActions(ScrolledForm form) {
        Action clearAction = new Action("Clear", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$
            public void run() {
                notifications.clear();
                viewer.refresh();
            }
        };
        clearAction
                .setToolTipText(Messages.NotificationsPage_clearActionToolTip);
        JMXImages.setLocalImageDescriptors(clearAction, "clear_co.gif"); //$NON-NLS-1$

        subscribeAction = new Action("Subscribe", Action.AS_CHECK_BOX) { //$NON-NLS-1$
            public void run() {
                toogleSubcription();
            }
        };
        subscribeAction
                .setToolTipText(Messages.NotificationsPage_subscribeActionToolTip);

        form.getToolBarManager().add(clearAction);
        form.getToolBarManager().add(subscribeAction);
        form.updateToolBar();
    }

    private void toogleSubcription() {
        if (subscribeAction.isChecked()) {
            try {
                wrapper.getMBeanServerConnection().addNotificationListener(
                        wrapper.getObjectName(), listener, null, null);
            } catch (Exception e) {
                JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
            }
        } else {
            try {
                wrapper.getMBeanServerConnection().removeNotificationListener(
                        wrapper.getObjectName(), listener);
            } catch (Exception e) {
                JMXUIActivator.log(IStatus.ERROR, e.getMessage(), e);
            }
        }
    }

    protected void createFormContent(IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText(wrapper.getObjectName().getCanonicalName());

        createToolBarActions(form);

        form.getForm().setSeparatorVisible(true);
        Composite body = form.getBody();
        body.setLayout(new GridLayout(1, false));
        Table table = toolkit.createTable(body, SWT.FULL_SELECTION);
        toolkit.paintBordersFor(body);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
        createColumns(table);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        viewer = new TableViewer(table);
        viewer.setLabelProvider(new NotificationLabelProvider());
        viewer.setContentProvider(new IStructuredContentProvider() {

            @SuppressWarnings("unchecked")//$NON-NLS-1$
            public Object[] getElements(Object inputElement) {
                return (Notification[]) notifications
                        .toArray(new Notification[notifications.size()]);
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput,
                    Object newInput) {
            }

        });
        viewer.setInput(notifications);
        listener = new NotificationListener() {
            @SuppressWarnings("unchecked")//$NON-NLS-1$
            public void handleNotification(final Notification notification,
                    Object handback) {
                notifications.add(notification);
                viewer.getControl().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        viewer.refresh();
                    }
                });
            }
        };
    }

    private void createColumns(final Table table) {
        final TableColumn timestampColumn = new TableColumn(table, SWT.NONE);
        timestampColumn.setText(Messages.NotificationsPage_timestamp);
        timestampColumn.setWidth(100);
        final TableColumn typeColumn = new TableColumn(table, SWT.NONE);
        typeColumn.setText(Messages.NotificationsPage_type);
        typeColumn.setWidth(200);
        final TableColumn messageColumn = new TableColumn(table, SWT.NONE);
        messageColumn.setText(Messages.NotificationsPage_message);
        messageColumn.setWidth(300);
        final TableColumn userDataColumn = new TableColumn(table, SWT.NONE);
        userDataColumn.setText(Messages.NotificationsPage_userData);
        userDataColumn.setWidth(100);
        final TableColumn sequenceNumberColumn = new TableColumn(table, SWT.NONE);
        sequenceNumberColumn.setText(Messages.NotificationsPage_sequenceNumber);
        sequenceNumberColumn.setWidth(100);
        final TableColumn sourceColumn = new TableColumn(table, SWT.NONE);
        sourceColumn.setText(Messages.NotificationsPage_source);
        sourceColumn.setWidth(100);
    }
}
