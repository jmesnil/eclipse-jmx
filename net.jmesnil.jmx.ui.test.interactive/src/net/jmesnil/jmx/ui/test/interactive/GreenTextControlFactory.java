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
 */
package net.jmesnil.jmx.ui.test.interactive;

import net.jmesnil.jmx.ui.extensions.IAttributeControlFactory;
import net.jmesnil.jmx.ui.extensions.IWritableAttributeHandler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class GreenTextControlFactory implements IAttributeControlFactory {

    public Control createControl(Composite parent, FormToolkit toolkit,
            boolean writable, String type, Object value,
            IWritableAttributeHandler handler) {

        final Text text = toolkit.createText(parent, value.toString(),
                SWT.BORDER);
        text.setEditable(false);
        text.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));
        return text;
    }
}
