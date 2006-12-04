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
package net.jmesnil.jmx.ui.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import net.jmesnil.jmx.ui.JMXUIActivator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Bundle of most images used by the Java plug-in.
 */
public class JMXImages {

    public static final IPath ICONS_PATH = new Path("$nl$/icons/full"); //$NON-NLS-1$

    private static final String NAME_PREFIX = "net.jmesnil.jmx.ui."; //$NON-NLS-1$

    private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

    // The plug-in registry
    private static ImageRegistry fgImageRegistry = null;

    private static HashMap<String, ImageDescriptor> fgAvoidSWTErrorMap = null;

    private static final String T_OBJ = "obj16"; //$NON-NLS-1$

    public static final String IMG_MISC_PUBLIC = NAME_PREFIX
            + "methpub_obj.gif"; //$NON-NLS-1$

    public static final String IMG_FIELD_PUBLIC = NAME_PREFIX
            + "field_public_obj.gif"; //$NON-NLS-1$

    public static final String IMG_OBJS_INTERFACE = NAME_PREFIX + "int_obj.gif"; //$NON-NLS-1$

    public static final String IMG_OBJS_PACKAGE = NAME_PREFIX
            + "package_obj.gif"; //$NON-NLS-1$

    public static final String IMG_OBJS_LIBRARY = NAME_PREFIX
            + "library_obj.gif"; //$NON-NLS-1$

    public static final ImageDescriptor DESC_MISC_PUBLIC = createManagedFromKey(
            T_OBJ, IMG_MISC_PUBLIC);

    public static final ImageDescriptor DESC_FIELD_PUBLIC = createManagedFromKey(
            T_OBJ, IMG_FIELD_PUBLIC);

    public static final ImageDescriptor DESC_OBJS_PACKAGE = createManagedFromKey(
            T_OBJ, IMG_OBJS_PACKAGE);

    public static final ImageDescriptor DESC_OBJS_INTERFACE = createManagedFromKey(
            T_OBJ, IMG_OBJS_INTERFACE);

    public static final ImageDescriptor DESC_OBJS_LIBRARY = createManagedFromKey(
            T_OBJ, IMG_OBJS_LIBRARY);

    public static Image get(String key) {
        return getImageRegistry().get(key);
    }

    private static ImageRegistry getImageRegistry() {
        if (fgImageRegistry == null) {
            fgImageRegistry = new ImageRegistry();
            for (Iterator<String> iter = fgAvoidSWTErrorMap.keySet().iterator(); iter
                    .hasNext();) {
                String key = iter.next();
                fgImageRegistry.put(key, fgAvoidSWTErrorMap
                        .get(key));
            }
            fgAvoidSWTErrorMap = null;
        }
        return fgImageRegistry;
    }

    private static ImageDescriptor createManagedFromKey(String prefix,
            String key) {
        return createManaged(prefix, key.substring(NAME_PREFIX_LENGTH), key);
    }

    private static ImageDescriptor createManaged(String prefix, String name,
            String key) {
        ImageDescriptor result = create(prefix, name, true);

        if (fgAvoidSWTErrorMap == null) {
            fgAvoidSWTErrorMap = new HashMap<String, ImageDescriptor>();
        }
        fgAvoidSWTErrorMap.put(key, result);
        return result;
    }

    private static ImageDescriptor create(String prefix, String name,
            boolean useMissingImageDescriptor) {
        IPath path = ICONS_PATH.append(prefix).append(name);
        return createImageDescriptor(JMXUIActivator.getDefault().getBundle(),
                path, useMissingImageDescriptor);
    }

    private static ImageDescriptor createImageDescriptor(Bundle bundle,
            IPath path, boolean useMissingImageDescriptor) {
        URL url = FileLocator.find(bundle, path, null);
        if (url != null) {
            return ImageDescriptor.createFromURL(url);
        }
        if (useMissingImageDescriptor) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
        return null;
    }
}
