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

import java.util.Arrays;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.Messages;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MBeanAttributesTable {

    protected class MBeanAttrContentProvider implements
            IStructuredContentProvider {
        private MBeanAttributeInfoWrapper[] attrs;

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            if (attrs == null)
                return new Object[0];
            return attrs;
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
            attrs = (MBeanAttributeInfoWrapper[]) newInput;
        }
    }

    protected class MBeanAttrLabelProvider extends LabelProvider implements
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
                return JMXImages.get(JMXImages.IMG_FIELD_PUBLIC);
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
            if (!(element instanceof MBeanAttributeInfoWrapper))
                return super.getText(element);

            MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) element;
            MBeanAttributeInfo attrInfo = wrapper.getMBeanAttributeInfo();
            switch (columnIndex) {
            case 0:
                return ""; //$NON-NLS-1$
            case 1:
                return attrInfo.getType();
            case 2:
                return attrInfo.getName();
            case 3:
                try {
                    MBeanServerConnection mbsc = wrapper
                            .getMBeanServerConnection();
                    ObjectName on = wrapper.getObjectName();
                    Object obj = mbsc.getAttribute(on, attrInfo.getName());
                    if (obj instanceof Object[]) {
                        return Arrays.asList((Object[]) obj).toString();
                    }
                    return obj.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return Messages.MBeanAttributesTable_unvailable;
                }
            }
            return getText(element);
        }
    }

    protected class MBeanAttrViewerSorter extends ViewerSorter {
        int fDirection, fIndex;

        protected MBeanAttrViewerSorter(int direction, int index) {
            fDirection = (direction == SWT.UP ? -1 : 1);
            fIndex = index;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
         *      java.lang.Object, java.lang.Object)
         */
        public int compare(Viewer viewer, Object e1, Object e2) {
            if (e1 instanceof MBeanAttributeInfoWrapper
                    && e2 instanceof MBeanAttributeInfoWrapper) {
                MBeanAttributeInfo attrInfo1 = ((MBeanAttributeInfoWrapper) e1)
                        .getMBeanAttributeInfo();
                MBeanAttributeInfo attrInfo2 = ((MBeanAttributeInfoWrapper) e2)
                        .getMBeanAttributeInfo();
                switch (fIndex) {
                case 1:
                    String a1 = attrInfo1.getType();
                    String a2 = attrInfo2.getType();
                    int p = a1.lastIndexOf('.');
                    if (p != -1)
                        a1 = a1.substring(p + 1);
                    p = a2.lastIndexOf('.');
                    if (p != -1)
                        a2 = a2.substring(p + 1);
                    return fDirection * a1.compareTo(a2);
                case 2:
                    return fDirection
                            * attrInfo1.getName()
                                    .compareTo(attrInfo2.getName());
                }
            }
            return fDirection * super.compare(viewer, e1, e2);
        }
    }

    private TableViewer viewer;

    public MBeanAttributesTable(Composite parent, final MBeanInfoView beanView) {
        final Table opTable = beanView.getToolkit().createTable(
                parent,
                SWT.BORDER | SWT.SINGLE | SWT.FLAT | SWT.FULL_SELECTION
                        | SWT.V_SCROLL | SWT.H_SCROLL);
        createColumns(opTable);
        opTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        opTable.setLinesVisible(true);
        opTable.setHeaderVisible(true);

        viewer = new TableViewer(opTable);
        viewer.setContentProvider(new MBeanAttrContentProvider());
        viewer.setLabelProvider(new MBeanAttrLabelProvider());
    }

    private void createColumns(final Table attrTable) {
        TableColumn blankCol = new TableColumn(attrTable, SWT.NONE);
        blankCol.setText(""); //$NON-NLS-1$
        blankCol.setWidth(20);
        final TableColumn returnType = new TableColumn(attrTable, SWT.NONE);
        returnType.setText(Messages.type);
        returnType.setWidth(100);
        final TableColumn attrName = new TableColumn(attrTable, SWT.NONE);
        attrName.setText(Messages.name);
        attrName.setWidth(150);
        final TableColumn attrValue = new TableColumn(attrTable, SWT.NONE);
        attrValue.setText(Messages.value);
        attrValue.setWidth(300);

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
                if (currentColumn == returnType)
                    colIndex = 1;
                else if (currentColumn == attrName)
                    colIndex = 2;
                else if (currentColumn == attrValue)
                    colIndex = 3;
                else
                    return;

                // sort the data based on column and direction
                attrTable.setSortDirection(dir);
                viewer.setSorter(new MBeanAttrViewerSorter(dir, colIndex));
            }
        };
        returnType.addListener(SWT.Selection, sortListener);
        attrName.addListener(SWT.Selection, sortListener);
        attrTable.setSortColumn(attrName);
        attrTable.setSortDirection(SWT.UP);
    }

    protected void setInput(MBeanInfoWrapper input) {
        if (input == null || input.getMBeanInfo() == null)
            viewer.setInput(null);
        else
            viewer.setInput(input.getMBeanAttributeInfoWrappers());
    }
}
