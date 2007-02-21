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

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class MBeanAttributesTable {

    private TableViewer viewer;

    public MBeanAttributesTable(Composite parent, final FormToolkit toolkit,
            final MBeanInfoView infoView) {
        final Table attrTable = toolkit.createTable(parent, SWT.BORDER
                | SWT.SINGLE | SWT.FLAT | SWT.FULL_SELECTION | SWT.V_SCROLL
                | SWT.H_SCROLL);
        toolkit.paintBordersFor(attrTable);
        createColumns(attrTable);
        attrTable.setLayoutData(new TableWrapData(TableWrapData.FILL));
        attrTable.setLinesVisible(true);
        attrTable.setHeaderVisible(true);
        attrTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.item == null || e.item.getData() == null)
                    return;
                infoView.selectionChanged(infoView, new StructuredSelection(
                        e.item.getData()));
            }
        });
        viewer = new TableViewer(attrTable);
        viewer.setContentProvider(new AttributesContentProvider());
        viewer.setLabelProvider(new AttributesLabelProvider());
    }

    private void createColumns(final Table attrTable) {
        final TableColumn attrName = new TableColumn(attrTable, SWT.NONE);
        attrName.setText(Messages.name);
        attrName.setWidth(150);
        final TableColumn attrValue = new TableColumn(attrTable, SWT.NONE);
        attrValue.setText(Messages.value);
        attrValue.setWidth(250);

        Listener sortListener = new Listener() {
            public void handleEvent(Event e) {
                // determine new sort column and direction
                TableColumn sortColumn = attrTable.getSortColumn();
                TableColumn currentColumn = (TableColumn) e.widget;

                int dir = attrTable.getSortDirection();
                if (sortColumn == currentColumn) {
                    dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                } else {
                    attrTable.setSortColumn(currentColumn);
                    dir = SWT.UP;
                }

                int colIndex;
                if (currentColumn == attrName)
                    colIndex = 0;
                else if (currentColumn == attrValue)
                    colIndex = 1;
                else
                    return;

                // sort the data based on column and direction
                attrTable.setSortDirection(dir);
                viewer.setSorter(new AttributesViewerSorter(dir, colIndex));
            }
        };
        attrName.addListener(SWT.Selection, sortListener);
        attrTable.setSortColumn(attrName);
        attrTable.setSortDirection(SWT.UP);
    }

    protected void setInput(MBeanInfoWrapper input) {
        if (input == null || input.getMBeanInfo() == null)
            viewer.setInput(null);
        else
            viewer.setInput(input.getMBeanAttributeInfoWrappers());
        viewer.getTable().redraw();
    }

    public Viewer getViewer() {
        return viewer;
    }
}
