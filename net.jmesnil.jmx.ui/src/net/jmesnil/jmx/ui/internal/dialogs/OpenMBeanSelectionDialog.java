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
import java.util.List;

import javax.management.MBeanServerConnection;

import net.jmesnil.jmx.core.JMXCoreActivator;
import net.jmesnil.jmx.resources.MBeanFeatureInfoWrapper;
import net.jmesnil.jmx.ui.internal.tree.Node;
import net.jmesnil.jmx.ui.internal.tree.NodeUtils;
import net.jmesnil.jmx.ui.internal.tree.ObjectNameNode;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorerContentProvider;
import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorerLabelProvider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
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

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);

        final FilteredTree filter = new FilteredTree(area, SWT.MULTI
                | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter());

        viewer = filter.getViewer();
        MBeanExplorerContentProvider contentProvider = new MBeanExplorerContentProvider();
        contentProvider.setFlatLayout(true);
        viewer.setContentProvider(contentProvider);
        MBeanExplorerLabelProvider labelProvider = new MBeanExplorerLabelProvider();
        labelProvider.setFlatLayout(true);
        viewer.setLabelProvider(labelProvider);
        MBeanServerConnection mbsc = JMXCoreActivator.getDefault()
                .getMBeanServerConnection();
        if (mbsc != null) {
            try {
                Node root = NodeUtils.createObjectNameTree(mbsc);
                viewer.setInput(root);
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
        if (selected instanceof ObjectNameNode) {
            ObjectNameNode node = (ObjectNameNode) selected;
            List results = new ArrayList();
            results.add(node.getMbeanInfoWrapper());
            setResult(results);
        }
        if (selected instanceof MBeanFeatureInfoWrapper) {
            MBeanFeatureInfoWrapper feature = (MBeanFeatureInfoWrapper) selected;
            List results = new ArrayList();
            results.add(feature);
            setResult(results);
        }
    }

}
