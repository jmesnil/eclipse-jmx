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

import java.lang.reflect.Array;

import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ArrayControlFactory extends AbstractTabularControlFactory {

	@Override
	protected void fillTable(final Table table, final Object value) {
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(400);
        
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(value, i);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(StringUtils.toString(element, false));
        }
	}

	@Override
	protected boolean getVisibleHeader() {
		return false;
	}

	@Override
	protected boolean getVisibleLines() {
		return true;
	}

}
