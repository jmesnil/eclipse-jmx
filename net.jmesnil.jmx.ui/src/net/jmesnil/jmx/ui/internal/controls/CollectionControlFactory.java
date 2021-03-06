/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Benjamin Walstrum (issue #24)
 *******************************************************************************/

package net.jmesnil.jmx.ui.internal.controls;

import java.util.Collection;

import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CollectionControlFactory extends AbstractTabularControlFactory {

	@Override
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	protected void fillTable(final Table table, final Object value) {
        TableColumn columnName = new TableColumn(table, SWT.NONE);
        columnName.setText(Messages.name);
        columnName.setWidth(400);
        
        for (Object element : (Collection) value) {
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
