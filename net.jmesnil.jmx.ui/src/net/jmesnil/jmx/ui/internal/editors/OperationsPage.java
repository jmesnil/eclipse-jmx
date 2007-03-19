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
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class OperationsPage extends FormPage {

    static final String ID = "operations"; //$NON-NLS-1$

    private OperationsBLock block;

    private MBeanInfoWrapper wrapper;

    public class OperationsBLock extends MasterDetailsBlock implements
            IDetailsPageProvider {

        private IFormPart masterSection;

        private IDetailsPage operationDetails;

        public OperationsBLock() {
        }

        protected void createMasterPart(IManagedForm managedForm,
                Composite parent) {
            masterSection = new OperationsSection(wrapper, managedForm, parent);
            managedForm.addPart(masterSection);
        }

        protected void registerPages(DetailsPart detailsPart) {
            operationDetails = new OperationDetails(masterSection);
            detailsPart.setPageLimit(10);
            detailsPart.setPageProvider(this);
            detailsPart.registerPage(MBeanOperationInfoWrapper.class,
                    operationDetails);
        }

        protected void createToolBarActions(IManagedForm managedForm) {
            ActionUtils.createLayoutActions(managedForm, sashForm);
        }

        public Object getPageKey(Object object) {
            return object;
        }

        public IDetailsPage getPage(Object key) {
            if (key instanceof MBeanOperationInfoWrapper) {
                return operationDetails;
            }
            return null;
        }
    }

    public OperationsPage(FormEditor editor) {
        super(editor, ID, Messages.OperationsPage_title);
        MBeanEditorInput input = (MBeanEditorInput) editor.getEditorInput();
        this.wrapper = input.getWrapper();
        block = new OperationsBLock();
    }

    protected void createFormContent(IManagedForm managedForm) {
        ScrolledForm form = managedForm.getForm();
        form.getForm().setSeparatorVisible(true);
        form.getForm().setText(wrapper.getObjectName().toString());
        block.createContent(managedForm);
    }
}