package net.jmesnil.jmx.ui.internal.views;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class AttributesDetailPage implements IDetailsPage {

    private Text nameText;

    private MBeanAttributeInfoWrapper attribute;

    private FormToolkit toolkit;

    public void initialize(IManagedForm form) {
        this.toolkit = form.getToolkit();
    }

    public void createContents(Composite parent) {
        GridLayout glayout = new GridLayout();
        glayout.numColumns = 2;
        parent.setLayout(glayout);
        Label label = toolkit.createLabel(parent, "Name");
        label.setLayoutData(new GridData(SWT.RIGHT));
        nameText = toolkit.createText(parent, "", SWT.READ_ONLY);
        nameText
                .setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
    }

    public void selectionChanged(IFormPart part, ISelection selection) {
        IStructuredSelection ssel = (IStructuredSelection) selection;
        attribute = (MBeanAttributeInfoWrapper) ssel.getFirstElement();
        update();
    }

    private void update() {
        Assert.isNotNull(attribute);

        nameText.setText(attribute.getMBeanAttributeInfo().getName());
        System.out.println(nameText.getText());
    }

    public void commit(boolean onSave) {
    }

    public void dispose() {
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isStale() {
        return false;
    }

    public void refresh() {
    }

    public void setFocus() {
    }

    public boolean setFormInput(Object input) {
        return false;
    }
}
