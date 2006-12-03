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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

class ViewUtil {

    static Composite createSection(String sectionTitle, String description,
            IManagedForm form, Composite parent, int cols, boolean fillBoth,
            boolean twistie) {
        FormToolkit toolkit = form.getToolkit();
        int flags = ExpandableComposite.TITLE_BAR;
        if (twistie)
            flags |= ExpandableComposite.TWISTIE;
        if (description != null)
            flags |= Section.DESCRIPTION;
        SectionPart sectionPart = new SectionPart(parent, toolkit, flags);
        sectionPart.initialize(form);
        Section section = sectionPart.getSection();
        section.setText(sectionTitle);
        if (description != null)
            section.setDescription(description);
        flags = fillBoth ? GridData.FILL_BOTH : GridData.FILL_HORIZONTAL;
        section.setLayoutData(new GridData(flags));
        Composite composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(cols, false));
        composite.setLayoutData(new GridData(flags));
        section.setClient(composite);
        if (twistie) {
            section.setEnabled(false);
            section.setExpanded(false);
        }
        return composite;
    }

    static IViewPart getView(String viewId) {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(viewId);
    }

    static MBeanOperationInvocationView getMBeanOperationInvocationView() {
        return (MBeanOperationInvocationView) getView(MBeanOperationInvocationView.ID);
    }

}
