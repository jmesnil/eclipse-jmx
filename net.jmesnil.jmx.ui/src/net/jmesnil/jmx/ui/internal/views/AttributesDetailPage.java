package net.jmesnil.jmx.ui.internal.views;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class AttributesDetailPage implements IDetailsPage {

    
    private MBeanAttributeInfoWrapper attribute;

    private FormToolkit toolkit;

    private Text nameText, descText, typeText;

    private Button readableCheckbox, writableCheckbox;

    public void initialize(IManagedForm form) {
        this.toolkit = form.getToolkit();
    }

    public void createContents(Composite parent) {
        GridLayout glayout = new GridLayout();
        glayout.numColumns = 2;
        parent.setLayout(glayout);
        Label label = toolkit.createLabel(parent, "Name");
        label.setLayoutData(new GridData(SWT.END));
        nameText = toolkit.createText(parent, "", SWT.READ_ONLY);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
        
        Label typeLabel = toolkit.createLabel(parent, "Type");
        typeLabel.setLayoutData(new GridData(SWT.RIGHT));
        typeText = toolkit.createText(parent, "", SWT.READ_ONLY);
        typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
        
        Label descLabel = toolkit.createLabel(parent, "Description");
        descLabel.setLayoutData(new GridData(SWT.RIGHT));
        descText = toolkit.createText(parent, "", SWT.READ_ONLY);
        descText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
        
        readableCheckbox = toolkit.createButton(parent, "Readable", SWT.CHECK);
        readableCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
        readableCheckbox.setEnabled(false);
        
        writableCheckbox = toolkit.createButton(parent, "Writable", SWT.CHECK);
        writableCheckbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | SWT.LEFT));
        writableCheckbox.setEnabled(false);
    }

    public void selectionChanged(IFormPart part, ISelection selection) {
        IStructuredSelection ssel = (IStructuredSelection) selection;
        attribute = (MBeanAttributeInfoWrapper) ssel.getFirstElement();
        update();
    }

    private void update() {
        Assert.isNotNull(attribute);

        nameText.setText(attribute.getMBeanAttributeInfo().getName());
        typeText.setText(attribute.getMBeanAttributeInfo().getType());
        descText.setText(attribute.getMBeanAttributeInfo().getDescription());
        readableCheckbox.setSelection(attribute.getMBeanAttributeInfo().isReadable());
        writableCheckbox.setSelection(attribute.getMBeanAttributeInfo().isWritable());
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
