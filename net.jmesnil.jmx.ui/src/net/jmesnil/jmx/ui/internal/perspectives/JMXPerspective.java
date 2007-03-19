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
 */
package net.jmesnil.jmx.ui.internal.perspectives;

import net.jmesnil.jmx.ui.internal.views.explorer.MBeanExplorer;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class JMXPerspective implements IPerspectiveFactory {

    private IPageLayout factory;

    public JMXPerspective() {
        super();
    }

    public void createInitialLayout(IPageLayout factory) {
        this.factory = factory;
        factory.setEditorAreaVisible(true);
        addViews();
        addViewShortcuts();
    }

    private void addViews() {
        IFolderLayout left = factory.createFolder("left", //$NON-NLS-1$
                IPageLayout.LEFT, 0.2f, factory.getEditorArea());
        left.addView(MBeanExplorer.ID);
    }

    private void addViewShortcuts() {
        factory.addShowViewShortcut(MBeanExplorer.ID);
        factory.addShowViewShortcut("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
    }

}
