package net.jmesnil.jmx.ui.internal.views;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class AttributesDetailPage implements IDetailsPage {

    
    private MBeanAttributeInfoWrapper attribute;

    private FormToolkit toolkit;

    private Text nameText, descText, typeText;

    private Button readableCheckbox, writableCheckbox;

    private Font bold;

    public void initialize(IManagedForm form) {
        this.toolkit = form.getToolkit();
    }

    public void createContents(Composite parent) {
        FontData fd[] = parent.getFont().getFontData();
        bold = new Font(parent.getDisplay(), fd[0].getName(), fd[0].height,
            SWT.BOLD);
        TableWrapLayout twlayout = new TableWrapLayout();
        twlayout.numColumns = 2;
        parent.setLayout(twlayout);
        
        Label label = toolkit.createLabel(parent, "Name");
        label.setLayoutData(new TableWrapData(TableWrapData.FILL));
        nameText = toolkit.createText(parent, "", SWT.READ_ONLY);
        nameText.setFont(bold);
        nameText.setLayoutData(new TableWrapData(TableWrapData.FILL));
        
        Label typeLabel = toolkit.createLabel(parent, "Type");
        typeLabel.setLayoutData(new TableWrapData(TableWrapData.FILL));
        typeText = toolkit.createText(parent, "", SWT.READ_ONLY);
        typeText.setFont(bold);
        typeText.setLayoutData(new TableWrapData(TableWrapData.FILL));
        
        Label descLabel = toolkit.createLabel(parent, "Description");
        descLabel.setLayoutData(new TableWrapData(TableWrapData.FILL));
        descText = toolkit.createText(parent, "", SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        descText.setFont(bold);
        TableWrapData twdata = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB);
        twdata.grabHorizontal = twdata.grabVertical = true;
        descText.setLayoutData(twdata);
        
        readableCheckbox = toolkit.createButton(parent, "Readable", SWT.CHECK);
        twdata = new TableWrapData(TableWrapData.FILL);
        twdata.colspan = 2;
        readableCheckbox.setLayoutData(twdata);
        readableCheckbox.setEnabled(false);
        
        writableCheckbox = toolkit.createButton(parent, "Writable", SWT.CHECK);
        twdata = new TableWrapData(TableWrapData.FILL);
        twdata.colspan = 2;
        writableCheckbox.setLayoutData(twdata);
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
        typeText.setText(StringUtils.toString(attribute.getMBeanAttributeInfo().getType()));
        descText.setText(attribute.getMBeanAttributeInfo().getDescription());
        readableCheckbox.setSelection(attribute.getMBeanAttributeInfo().isReadable());
        writableCheckbox.setSelection(attribute.getMBeanAttributeInfo().isWritable());
    }

    public void commit(boolean onSave) {
    }

    public void dispose() {
      bold.dispose();
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
