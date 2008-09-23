/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.editors;

import net.jmesnil.jmx.core.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.core.MBeanInfoWrapper;
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

public class AttributesPage extends FormPage {

    static final String ID = "attributes"; //$NON-NLS-1$

    private AttributesBLock block;

    private MBeanInfoWrapper wrapper;

    public class AttributesBLock extends MasterDetailsBlock implements
            IDetailsPageProvider {

        private IFormPart masterSection;

        private IDetailsPage attributeDetails;

        public AttributesBLock() {
        }

        protected void createMasterPart(IManagedForm managedForm,
                Composite parent) {
            masterSection = new AttributesSection(wrapper, managedForm, parent);
            managedForm.addPart(masterSection);
        }

        protected void registerPages(DetailsPart detailsPart) {
            attributeDetails = new AttributeDetails(masterSection);
            detailsPart.setPageLimit(10);
            detailsPart.setPageProvider(this);
            detailsPart.registerPage(MBeanAttributeInfoWrapper.class,
                    attributeDetails);
        }

        protected void createToolBarActions(IManagedForm managedForm) {
            ActionUtils.createLayoutActions(managedForm, sashForm);
        }

        public Object getPageKey(Object object) {
            return object;
        }

        public IDetailsPage getPage(Object key) {
            if (key instanceof MBeanAttributeInfoWrapper) {
                return attributeDetails;
            }
            return null;
        }
    }

    public AttributesPage(FormEditor editor) {
        super(editor, ID, Messages.AttributesPage_title);
        MBeanEditorInput input = (MBeanEditorInput) editor.getEditorInput();
        this.wrapper = input.getWrapper();
        block = new AttributesBLock();
    }

    protected void createFormContent(IManagedForm managedForm) {
        ScrolledForm form = managedForm.getForm();
        form.getForm().setSeparatorVisible(true);
        form.getForm().setText(wrapper.getObjectName().toString());
        block.createContent(managedForm);
    }

    @Override
    public boolean selectReveal(Object object) {
        if (object instanceof MBeanAttributeInfoWrapper) {
            MBeanAttributeInfoWrapper attrWrapper = (MBeanAttributeInfoWrapper) object;
            getEditor().setActivePage(ID);
            return block.masterSection.setFormInput(attrWrapper);
        }
        return super.selectReveal(object);
    }
}