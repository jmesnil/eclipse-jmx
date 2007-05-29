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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import net.jmesnil.jmx.ui.extensions.IAttributeControlFactory;
import net.jmesnil.jmx.ui.extensions.IWritableAttributeHandler;

public class BooleanControlFactory implements IAttributeControlFactory {

	public Control createControl(final Composite parent, final FormToolkit toolkit,
			final boolean writable, final String type, final Object value, 
			final IWritableAttributeHandler handler) {

		boolean booleanValue = ((Boolean) value).booleanValue();
        if (!writable) {
            if (toolkit != null) {
                return toolkit.createText(parent, Boolean
                        .toString(booleanValue), SWT.SINGLE);
            } else {
                Text text = new Text(parent, SWT.SINGLE);
                text.setText(Boolean.toString(booleanValue));
                return text;
            }
        }

        final Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        if (toolkit != null) {
            combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
            toolkit.paintBordersFor(combo);
        }
        combo.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        combo.setItems(new String[] { Boolean.TRUE.toString(),
                Boolean.FALSE.toString() });
        if (booleanValue) {
            combo.select(0);
        } else {
            combo.select(1);
        }
        if (handler != null) {
            combo.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    Boolean newValue = Boolean.valueOf(combo.getText());
                    handler.write(newValue);
                }
            });
        }
        return combo;
	}
	
}
