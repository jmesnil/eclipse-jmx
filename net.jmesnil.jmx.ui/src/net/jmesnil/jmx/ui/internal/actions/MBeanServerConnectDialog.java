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
 */
package net.jmesnil.jmx.ui.internal.actions;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

public class MBeanServerConnectDialog extends SelectionDialog {

    // sizing constants
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    private Text hostText, portText, urlText;

    private Text userNameText, passwordText;

    private Text advancedUserNameText, advancedPasswordText;

    private Shell parent;

    private String url, userName, password;

    private TabFolder folder;

    public MBeanServerConnectDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.ConnectionSelectionDialog_title);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        this.parent = parent.getShell();
        Composite composite = (Composite) super.createDialogArea(parent);
        Font font = parent.getFont();
        composite.setFont(font);

        // createMessageArea(composite);

        folder = new TabFolder(composite, SWT.TOP);
        TabItem simpleItem = new TabItem(folder, SWT.NONE);
        simpleItem.setText(Messages.MBeanServerConnectDialog_simpleTab);
        simpleItem.setControl(createSimpleConnectionPage(folder));

        TabItem advancedItem = new TabItem(folder, SWT.NONE);
        advancedItem.setText(Messages.MBeanServerConnectDialog_advancedTab);
        advancedItem.setControl(createAdvancedConnectionPage(folder));

        return composite;
    }

    private Control createSimpleConnectionPage(Composite parent) {
        Composite fieldComposite = new Composite(parent, SWT.NULL);
        fieldComposite.setLayout(new GridLayout(2, false));

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

        // 1 host label
        Label label = new Label(fieldComposite, SWT.CENTER);
        label.setText(Messages.ConnectionSelectionDialog_host);
        // 2 host text entry
        hostText = new Text(fieldComposite, SWT.BORDER);
        hostText.setText("localhost"); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(25);
        hostText.setLayoutData(data);
        // 3 port label
        label = new Label(fieldComposite, SWT.CENTER);
        label.setText(Messages.ConnectionSelectionDialog_port);
        // 4 port text entry
        portText = new Text(fieldComposite, SWT.BORDER);
        portText.setTextLimit(5);
        portText.setText("3000"); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(6);
        portText.setLayoutData(data);
        // 5 user name label
        label = new Label(fieldComposite, SWT.CENTER);
        label.setText("User Name");
        // 6 user name text entry
        userNameText = new Text(fieldComposite, SWT.BORDER);
        userNameText.setText(""); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(25);
        userNameText.setLayoutData(data);
        // 7 password label
        label = new Label(fieldComposite, SWT.CENTER);
        label.setText("Password");
        // 8 user name text entry
        passwordText = new Text(fieldComposite, SWT.BORDER | SWT.PASSWORD);
        passwordText.setText(""); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(25);
        passwordText.setLayoutData(data);

        return fieldComposite;

    }

    private Control createAdvancedConnectionPage(Composite parent) {
        Composite fieldComposite = new Composite(parent, SWT.NULL);
        fieldComposite.setLayout(new GridLayout(2, false));

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

        // 1 host label
        Label label = new Label(fieldComposite, SWT.CENTER);
        label.setText("JMX URL"); //$NON-NLS-1$
        // 2 URL text entry
        urlText = new Text(fieldComposite, SWT.BORDER);
        urlText.setText("service:jmx:"); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = convertWidthInCharsToPixels(25);
        urlText.setLayoutData(data);
        // 3 user name label
        label = new Label(fieldComposite, SWT.CENTER);
        label.setText("User Name");
        // 4 user name text entry
        advancedUserNameText = new Text(fieldComposite, SWT.BORDER);
        advancedUserNameText.setText(""); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(25);
        advancedUserNameText.setLayoutData(data);
        // 5 password label
        label = new Label(fieldComposite, SWT.CENTER);
        label.setText("Password");
        // 6 user name text entry
        advancedPasswordText = new Text(fieldComposite, SWT.BORDER
                | SWT.PASSWORD);
        advancedPasswordText.setText(""); //$NON-NLS-1$
        data = new GridData();
        data.widthHint = convertWidthInCharsToPixels(25);
        advancedPasswordText.setLayoutData(data);

        return fieldComposite;
    }

    @Override
    protected void okPressed() {
        if (folder.getSelectionIndex() == 0) {
            userName = userNameText.getText();
            password = passwordText.getText();
            if (hostText.getText().equals("")) { //$NON-NLS-1$
                MessageDialog.openError(parent.getShell(),
                        Messages.ConnectionSelectionDialog_error,
                        Messages.ConnectionSelectionDialog_invalid_host);
                return;
            }
            try {
                InetAddress.getByName(hostText.getText());
            } catch (UnknownHostException e) {
                MessageDialog.openError(parent.getShell(),
                        Messages.ConnectionSelectionDialog_error,
                        Messages.ConnectionSelectionDialog_invalid_host);
                return;
            }
            String host = hostText.getText();
            if (portText.getText().equals("")) { //$NON-NLS-1$
                MessageDialog.openError(parent.getShell(),
                        Messages.ConnectionSelectionDialog_error,
                        Messages.ConnectionSelectionDialog_invalid_port);
                return;
            }
            int port;
            try {
                port = Integer.parseInt(portText.getText());
                if (port < 1 || port > 0xffff) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                MessageDialog.openError(parent.getShell(),
                        Messages.ConnectionSelectionDialog_error,
                        Messages.ConnectionSelectionDialog_invalid_port);
                return;
            }
            url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        if (folder.getSelectionIndex() == 1) {
            userName = advancedUserNameText.getText();
            password = advancedPasswordText.getText();

            if (urlText.getText().equals("")) { //$NON-NLS-1$
                MessageDialog.openError(parent.getShell(),
                        Messages.ConnectionSelectionDialog_error,
                        Messages.ConnectionSelectionDialog_invalid_url);
                return;
            }
            url = urlText.getText();
        }
        super.okPressed();
    }

    String getURL() {
        return url;
    }

    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }
}