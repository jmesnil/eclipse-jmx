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

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.resources.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;
import net.jmesnil.jmx.ui.internal.StringUtils;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class MBeanOperationsTable {

    protected class MBeanOpContentProvider implements
            IStructuredContentProvider {
        private MBeanOperationInfoWrapper[] opInfos;

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            if (opInfos == null)
                return new Object[0];
            return opInfos;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
            // nothing needs to be disposed
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         *      java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            opInfos = (MBeanOperationInfoWrapper[]) newInput;
        }
    }

    protected class MBeanOpLabelProvider extends LabelProvider implements
            ITableLabelProvider {
        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
         *      int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            switch (columnIndex) {
            case 0:
                return JMXImages.get(JMXImages.IMG_MISC_PUBLIC);
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
         *      int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if (!(element instanceof MBeanOperationInfoWrapper))
                return super.getText(element);

            MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) element;
            MBeanOperationInfo opInfo = wrapper.getMBeanOperationInfo();
            switch (columnIndex) {
            case 0:
                return ""; //$NON-NLS-1$
            case 1:
                return opInfo.getReturnType() != null ? StringUtils
                        .toString(opInfo.getReturnType()) : "void"; //$NON-NLS-1$
            case 2:
                return opInfo.getName();
            case 3:
                MBeanParameterInfo[] params = opInfo.getSignature();
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < params.length; j++) {
                    String type = params[j].getType();
                    if (j != 0)
                        sb.append(", "); //$NON-NLS-1$
                    sb.append(StringUtils.toString(type));
                }
                return sb.toString();
            }
            return getText(element);
        }
    }

    protected class MBeanOpViewerSorter extends ViewerSorter {
        int fDirection, fIndex;

        protected MBeanOpViewerSorter(int direction, int index) {
            fDirection = direction == SWT.UP ? -1 : 1;
            fIndex = index;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
         *      java.lang.Object, java.lang.Object)
         */
        public int compare(Viewer viewer, Object e1, Object e2) {
            if (e1 instanceof MBeanOperationInfo
                    && e2 instanceof MBeanOperationInfo) {
                MBeanOperationInfo opInfo1 = ((MBeanOperationInfoWrapper) e1)
                        .getMBeanOperationInfo();
                MBeanOperationInfo opInfo2 = ((MBeanOperationInfoWrapper) e2)
                        .getMBeanOperationInfo();
                switch (fIndex) {
                case 1:
                    String a1 = opInfo1.getReturnType();
                    String a2 = opInfo2.getReturnType();
                    int p = a1.lastIndexOf('.');
                    if (p != -1)
                        a1 = a1.substring(p + 1);
                    p = a2.lastIndexOf('.');
                    if (p != -1)
                        a2 = a2.substring(p + 1);
                    return fDirection * a1.compareTo(a2);
                case 2:
                    return fDirection
                            * opInfo1.getName().compareTo(opInfo2.getName());
                case 3:
                    MBeanParameterInfo[] info1 = opInfo1.getSignature();
                    MBeanParameterInfo[] info2 = opInfo2.getSignature();
                    if (info2.length == 0)
                        return fDirection;
                    if (info1.length == 0)
                        return -fDirection;

                    return fDirection
                            * (info1[0].getType().compareTo(info2[0].getType()));
                }
            }
            return fDirection * super.compare(viewer, e1, e2);
        }
    }

    private TableViewer viewer;

    public MBeanOperationsTable(Composite parent, final FormToolkit toolkit) {
        final Table operationsTable = toolkit.createTable(parent, SWT.BORDER
                | SWT.SINGLE | SWT.FLAT | SWT.FULL_SELECTION | SWT.V_SCROLL
                | SWT.H_SCROLL);
        createColumns(operationsTable);
        toolkit.paintBordersFor(operationsTable);
        TableWrapData twdata = new TableWrapData(TableWrapData.FILL_GRAB);
        twdata.colspan = 2;
        operationsTable.setLayoutData(twdata);
        operationsTable.setLinesVisible(true);
        operationsTable.setHeaderVisible(true);
        operationsTable.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (e.item == null || e.item.getData() == null)
                    return;
                MBeanOperationInvocationView invocationView = ViewUtil
                        .getMBeanOperationInvocationView();
                invocationView.selectionChanged(null, new StructuredSelection(
                        e.item.getData()));
            }
        });

        viewer = new TableViewer(operationsTable);
        viewer.setContentProvider(new MBeanOpContentProvider());
        viewer.setLabelProvider(new MBeanOpLabelProvider());
    }

    private void createColumns(final Table opTable) {
        TableColumn blankCol = new TableColumn(opTable, SWT.NONE);
        blankCol.setText(""); //$NON-NLS-1$
        blankCol.setWidth(20);
        final TableColumn returnType = new TableColumn(opTable, SWT.NONE);
        returnType.setText(Messages.returnType);
        returnType.setWidth(100);
        final TableColumn opName = new TableColumn(opTable, SWT.NONE);
        opName.setText(Messages.name);
        opName.setWidth(150);
        final TableColumn params = new TableColumn(opTable, SWT.NONE);
        params.setText(Messages.parameters);
        params.setWidth(300);

        Listener sortListener = new Listener() {
            public void handleEvent(Event e) {
                // determine new sort column and direction
                TableColumn sortColumn = opTable.getSortColumn();
                TableColumn currentColumn = (TableColumn) e.widget;

                int dir = opTable.getSortDirection();
                if (sortColumn == currentColumn) {
                    dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                } else {
                    opTable.setSortColumn(currentColumn);
                    dir = SWT.UP;
                }
                int colIndex;
                if (currentColumn == returnType)
                    colIndex = 1;
                else if (currentColumn == opName)
                    colIndex = 2;
                else if (currentColumn == params)
                    colIndex = 3;
                else
                    return;

                // sort the data based on column and direction
                opTable.setSortDirection(dir);
                viewer.setSorter(new MBeanOpViewerSorter(dir, colIndex));
            }
        };
        returnType.addListener(SWT.Selection, sortListener);
        opName.addListener(SWT.Selection, sortListener);
        params.addListener(SWT.Selection, sortListener);
        opTable.setSortColumn(opName);
        opTable.setSortDirection(SWT.UP);
    }

    protected void setInput(MBeanInfoWrapper input) {
        if (input == null || input.getMBeanInfo() == null)
            viewer.setInput(null);
        else
            viewer.setInput(input.getMBeanOperationInfoWrappers());
    }
}
