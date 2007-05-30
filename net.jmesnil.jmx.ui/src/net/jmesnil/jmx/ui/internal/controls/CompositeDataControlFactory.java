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

import javax.management.openmbean.CompositeData;

import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CompositeDataControlFactory extends AbstractTabularControlFactory {

	@Override
	protected void fillTable(final Table table, final Object value) {
        TableColumn keyColumn = new TableColumn(table, SWT.NONE);
        keyColumn.setText(Messages.key);
        keyColumn.setWidth(150);
        TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setText(Messages.value);
        valueColumn.setWidth(250);
        
        CompositeData data = (CompositeData) value;
        for (Object o : data.getCompositeType().keySet()) {
            String key = (String) o;
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, key);
            item.setText(1, StringUtils.toString(data.get(key), false));
        }
	}

	@Override
	protected boolean getVisibleHeader() {
		return true;
	}

	@Override
	protected boolean getVisibleLines() {
		return true;
	}

}
