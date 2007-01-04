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

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.internal.MBeanUtils;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

public class MBeanOperationInvocationView extends ViewPart implements
        ISelectionListener {

    public static final String ID = "net.jmesnil.jmx.ui.internal.views.MBeanOperationInvocationView"; //$NON-NLS-1$

    private Composite invocationComposite;

    private ManagedForm managedForm;

    private MBeanOperationInfoWrapper opInfoWrapper;

    private Composite parentComp;

    @Override
    public void createPartControl(Composite parent) {
        parentComp = parent;
        getSite().getPage().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        if (managedForm != null)
            managedForm.dispose();
        getSite().getPage().removePostSelectionListener(this);
        super.dispose();
    }

    @Override
    public void setFocus() {
        if (managedForm != null)
            managedForm.setFocus();
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (!(selection instanceof IStructuredSelection))
            return;

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof MBeanOperationInfoWrapper) {
            MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) obj;
            if (wrapper == opInfoWrapper) {
                return;
            }
            // update the currently selected contribution to the one to be
            // displayed, if null
            // the controls displayed are still disposed, this is to reflect
            // a removed contribution
            opInfoWrapper = wrapper;
            drawInvocationDetails(wrapper);
        } else {
            clear();
        }
    }

    public void clear() {
        drawInvocationDetails(null);
    }

    protected void drawInvocationDetails(MBeanOperationInfoWrapper wrapper) {
        if (parentComp != null && !parentComp.isDisposed()) {
            // remove any controls created from prior selections
            Control[] childs = parentComp.getChildren();
            if (childs.length > 0) {
                for (int i = 0; i < childs.length; i++) {
                    childs[i].dispose();
                }
            }
        }
        if (wrapper == null) {
            return;
        }
        MBeanOperationInfo opInfo = wrapper.getMBeanOperationInfo();
        if (managedForm != null) {
            managedForm.dispose();
        }
        managedForm = new ManagedForm(parentComp);
        Composite body = managedForm.getForm().getBody();
        body.setLayout(new GridLayout());
        body.setLayoutData(new GridData(GridData.FILL_BOTH));

        invocationComposite = ViewUtil.createSection(
                Messages.MBeanOperationInvocationView_invocationTitle, null,
                managedForm, body, 1, true, false);

        FormToolkit toolkit = managedForm.getToolkit();
        String desc = opInfo.getDescription();
        // do not display the description if it is set to the operation name
        if (desc != null && !desc.equals(opInfo.getName())) {
            Composite c = toolkit
                    .createComposite(invocationComposite, SWT.NONE);
            c.setLayout(new GridLayout());
            toolkit.createLabel(c, desc);
        }
        // composite for method signature [ return type | method button | ( |
        // Composite(1..n parameters) |�) ]
        Composite c = toolkit.createComposite(invocationComposite, SWT.NONE);
        c.setLayout(new GridLayout(5, false));
        // return type
        toolkit.createLabel(c, opInfo.getReturnType() != null ? StringUtils
                .toString(opInfo.getReturnType()) : "void"); //$NON-NLS-1$
        // method name
        InvokeOperationButton invocationButton = new InvokeOperationButton(c,
                SWT.PUSH);
        toolkit.createLabel(c, "("); //$NON-NLS-1$
        // parameters
        final MBeanParameterInfo[] params = opInfo.getSignature();
        Text[] textParams = null;
        if (params.length > 0) {
            Composite paramsComposite = toolkit.createComposite(c, SWT.NONE);
            paramsComposite.setLayout(new GridLayout(
                    params.length + 1 /* button */, false));
            textParams = new Text[params.length];
            for (int j = 0; j < params.length; j++) {
                if (j > 0) {
                    toolkit.createLabel(paramsComposite, ", "); //$NON-NLS-1$
                }
                MBeanParameterInfo param = params[j];
                textParams[j] = new Text(paramsComposite, SWT.SINGLE
                        | SWT.BORDER);
                textParams[j].setText(StringUtils.toString(param.getType()));
                textParams[j].setLayoutData(new GridData(
                        GridData.GRAB_HORIZONTAL
                                | GridData.HORIZONTAL_ALIGN_FILL));
            }
            paramsComposite.pack();
        }
        toolkit.createLabel(c, ")"); //$NON-NLS-1$
        invocationButton.setTextParams(textParams);
        invocationComposite.pack();
        parentComp.layout();
    }

    private class InvokeOperationButton extends SelectionAdapter {

        private Text[] textParams;

        private Button button;

        public InvokeOperationButton(Composite parent, int style) {
            button = managedForm.getToolkit().createButton(parent,
                    opInfoWrapper.getMBeanOperationInfo().getName(), style);
            button.addSelectionListener(this);
        }

        void setTextParams(Text[] textParams) {
            this.textParams = textParams;
        }

        @Override
        public void widgetSelected(SelectionEvent event) {
            try {
                MBeanParameterInfo[] paramInfos = opInfoWrapper
                        .getMBeanOperationInfo().getSignature();
                Object[] paramList = null;
                if (textParams != null) {
                    String[] strs = new String[textParams.length];
                    for (int i = 0; i < strs.length; i++) {
                        strs[i] = textParams[i].getText();
                    }
                    paramList = MBeanUtils.getParameters(strs, paramInfos);
                }
                MBeanServerConnection mbsc = opInfoWrapper
                        .getMBeanServerConnection();
                ObjectName objectName = opInfoWrapper.getObjectName();
                String methodName = opInfoWrapper.getMBeanOperationInfo()
                        .getName();
                Object result;
                if (paramList != null) {
                    String[] paramSig = new String[paramInfos.length];
                    for (int i = 0; i < paramSig.length; i++) {
                        paramSig[i] = paramInfos[i].getType();
                    }
                    result = mbsc.invoke(objectName, methodName, paramList,
                            paramSig);
                } else {
                    result = mbsc.invoke(objectName, methodName, new Object[0],
                            new String[0]);
                }
                if ("void".equals(opInfoWrapper.getMBeanOperationInfo() //$NON-NLS-1$
                        .getReturnType())) {
                    result = Messages.MBeanOperationInvocationView_success;
                }
                MessageDialog.openInformation(managedForm.getForm().getShell(),
                        Messages.MBeanOperationInvocationView_result,
                        "" + result); //$NON-NLS-1$
            } catch (Exception e) {
                String message = e.getLocalizedMessage();
                // if the exception has a cause, it is likely more interesting
                // since it may be the exception thrown by the mbean implementation
                // rather than the exception thrown by the mbean server connection
                if (e.getCause() != null) {
                    message = e.getCause().getLocalizedMessage();
                }
                MessageDialog.openError(managedForm.getForm().getShell(),
                        Messages.MBeanOperationInvocationView_error, message);
            }
        }
    }

}
