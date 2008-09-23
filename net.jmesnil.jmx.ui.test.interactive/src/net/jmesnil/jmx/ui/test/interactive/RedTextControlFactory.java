/*******************************************************************************
 * Copyright (c) 2007 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package net.jmesnil.jmx.ui.test.interactive;

import net.jmesnil.jmx.ui.extensions.IAttributeControlFactory;
import net.jmesnil.jmx.ui.extensions.IWritableAttributeHandler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RedTextControlFactory implements IAttributeControlFactory {

    public Control createControl(Composite parent, FormToolkit toolkit,
            boolean writable, String type, Object value,
            IWritableAttributeHandler handler) {

        final Text text = toolkit.createText(parent, value.toString(),
                SWT.BORDER);
        text.setEditable(false);
        text.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        return text;
    }
}
