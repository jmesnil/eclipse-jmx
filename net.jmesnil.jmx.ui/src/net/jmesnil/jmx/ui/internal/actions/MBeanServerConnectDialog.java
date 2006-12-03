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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

public class MBeanServerConnectDialog extends SelectionDialog {

    // sizing constants
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    private Text hostText, portText;

    private Shell parent;

    private int port;

    private String host;

    public MBeanServerConnectDialog(Shell parentShell) {
        super(parentShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.ConnectionSelectionDialog_title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(final Composite parent) {
        this.parent = parent.getShell();
        Composite composite = (Composite) super.createDialogArea(parent);
        Font font = parent.getFont();
        composite.setFont(font);

        createMessageArea(composite);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

        Composite fieldComposite = new Composite(composite, SWT.NULL);
        fieldComposite.setLayout(new GridLayout(5, false));
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
        return composite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    protected void okPressed() {
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
        host = hostText.getText();
        if (portText.getText().equals("")) { //$NON-NLS-1$
            MessageDialog.openError(parent.getShell(),
                    Messages.ConnectionSelectionDialog_error,
                    Messages.ConnectionSelectionDialog_invalid_port);
            return;
        }
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
        super.okPressed();
    }

    String getHost() {
        return host;
    }

    int getPort() {
        return port;
    }
}