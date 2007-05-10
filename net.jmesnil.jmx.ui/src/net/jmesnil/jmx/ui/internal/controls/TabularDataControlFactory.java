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

import java.util.Set;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TabularDataControlFactory extends AbstractTabularControlFactory {

	@Override
	@SuppressWarnings("unchecked")//$NON-NLS-1$
	protected void fillTable(final Table table, final Object value) {
		TabularData data = (TabularData) value;
		
		Set keySet = data.getTabularType().getRowType().keySet();
		
        for (Object o : keySet) {
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText((String) o);
            column.setWidth(150);
            column.setMoveable(true);
            column.setResizable(true);
        }
        
        for (Object o : data.values()) {
            CompositeData rowData = (CompositeData) o;
            TableItem item = new TableItem(table, SWT.NONE);

            int i = 0;
            for (Object o2 : keySet) {
                String key = (String) o2;
                item.setText(i, StringUtils.toString(rowData.get(key), false));
                i++;
            }
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
