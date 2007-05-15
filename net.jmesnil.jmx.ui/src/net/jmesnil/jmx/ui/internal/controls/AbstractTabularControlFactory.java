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
 * 
 * Contributors:
 *      Benjamin Walstrum (issue #24)
 */
package net.jmesnil.jmx.ui.internal.controls;

import net.jmesnil.jmx.ui.extensions.IAttributeControlFactory;
import net.jmesnil.jmx.ui.extensions.IWritableAttributeHandler;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class AbstractTabularControlFactory 
		implements IAttributeControlFactory {

	public Control createControl(final Composite parent, final FormToolkit toolkit,
			final boolean writable, final String type, final Object value, 
			final IWritableAttributeHandler handler) {

		int style = SWT.SINGLE | SWT.FULL_SELECTION;
        Table table = null;
        if (toolkit != null) {
            table = toolkit.createTable(parent, style);
            toolkit.paintBordersFor(parent);
        } else {
            table = new Table(parent, style | SWT.BORDER);
        }
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 20;
        gd.widthHint = 100;
        table.setLayoutData(gd);
        
        if (value == null) {
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setWidth(200);
            column.setMoveable(false);
            column.setResizable(true);
            
            table.setHeaderVisible(false);
            table.setLinesVisible(getVisibleLines());
            
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.NULL);
            
        } else {
            fillTable(table, value);
            table.setHeaderVisible(getVisibleHeader());
            table.setLinesVisible(getVisibleLines());
        }
        
        return table;
	}

	protected abstract void fillTable(Table table, Object value);
	
	protected abstract boolean getVisibleHeader();
	
	protected abstract boolean getVisibleLines();
}
