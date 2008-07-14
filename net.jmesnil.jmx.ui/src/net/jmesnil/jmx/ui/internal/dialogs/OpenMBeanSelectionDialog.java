/**
 * Eclipse JMX Console
 * Copyright (C) 2007 Jeff Mesnil
 * Contact: http://www.jmesnil.net
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
 */

package net.jmesnil.jmx.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.jmesnil.jmx.resources.MBeanInfoWrapper;
import net.jmesnil.jmx.ui.JMXUIActivator;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorerLabelProvider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class OpenMBeanSelectionDialog extends SelectionStatusDialog {

    private TreeViewer viewer;

    public OpenMBeanSelectionDialog(Shell parent) {
        super(parent);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected Point getInitialSize() {
        Point result = super.getInitialSize();

        Point size = new Point(480, 320);
        result.x = Math.max(result.x, size.x);
        result.y = Math.max(result.y, size.y);
        Rectangle display = getShell().getDisplay().getClientArea();
        result.x = Math.min(result.x, display.width);
        result.y = Math.min(result.y, display.height);

        return result;
    }

    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);

        final FilteredTree filter = new FilteredTree(area, SWT.MULTI
                | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter());

        viewer = filter.getViewer();
        viewer.setContentProvider(new ContentProvider());
        viewer.setLabelProvider(new MBeanExplorerLabelProvider());
        
        MBeanServerConnection mbsc = JMXUIActivator.getDefault().getCurrentConnection();
                
        if (mbsc != null) {
            try {
                Set set = mbsc.queryNames(ObjectName.getInstance("*:*"), null); //$NON-NLS-1$
                List mbeans = new ArrayList();
                Iterator iter = set.iterator();
                while (iter.hasNext()) {
                    ObjectName objectName = (ObjectName) iter.next();
                    MBeanInfo info = mbsc.getMBeanInfo(objectName);
                    mbeans.add(new MBeanInfoWrapper(objectName, info, mbsc));
                }
                Collections.sort(mbeans);
                viewer.setInput(mbeans);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return area;
    }

    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void computeResult() {
        IStructuredSelection selection = (IStructuredSelection) viewer
                .getSelection();
        Object selected = selection.getFirstElement();
        if (selected == null) {
            setResult(null);
            return;
        }
        if (selected instanceof MBeanInfoWrapper) {
            MBeanInfoWrapper wrapper = (MBeanInfoWrapper) selected;
            List results = new ArrayList();
            results.add(wrapper);
            setResult(results);
        }
    }

    class ContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        private List mbeans;

        ContentProvider() {
        }

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            this.mbeans = (List) newInput;
        }

        public void dispose() {
        }

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        public Object[] getElements(Object parent) {
            if (mbeans != null) {
                return mbeans
                        .toArray(new MBeanInfoWrapper[mbeans.size()]);
            }
            return new Object[0];
        }

        public Object getParent(Object child) {
            return null;
        }

        @SuppressWarnings("unchecked")//$NON-NLS-1$
        public Object[] getChildren(Object parent) {
            return new Object[0];
        }

        public boolean hasChildren(Object parent) {
            return false;
        }
    }
}
