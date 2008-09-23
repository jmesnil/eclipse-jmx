/**
 * Eclipse JMX Console
 * Copyright (C) 2006 Jeff Mesnil
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
package net.jmesnil.jmx.ui.internal.views.navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.jmesnil.jmx.core.IConnectionProvider;
import net.jmesnil.jmx.core.IConnectionWrapper;
import net.jmesnil.jmx.core.MBeanAttributeInfoWrapper;
import net.jmesnil.jmx.core.MBeanInfoWrapper;
import net.jmesnil.jmx.core.MBeanOperationInfoWrapper;
import net.jmesnil.jmx.core.tree.DomainNode;
import net.jmesnil.jmx.core.tree.ObjectNameNode;
import net.jmesnil.jmx.core.tree.PropertyNode;
import net.jmesnil.jmx.ui.UIExtensionManager;
import net.jmesnil.jmx.ui.UIExtensionManager.ConnectionProviderUI;
import net.jmesnil.jmx.ui.internal.JMXImages;
import net.jmesnil.jmx.ui.internal.MBeanUtils;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class MBeanExplorerLabelProvider extends LabelProvider {
	private static ArrayList<MBeanExplorerLabelProvider> instances =
		new ArrayList<MBeanExplorerLabelProvider>();
	private static HashMap<String, Image> images =
		new HashMap<String, Image>();

	public MBeanExplorerLabelProvider() {
		super();
		instances.add(this);
	}

    public void dispose() {
		instances.remove(this);
		if( instances.isEmpty()) {
	    	for( Iterator<Image> i = images.values().iterator(); i.hasNext(); )
	    		i.next().dispose();
		}
    	super.dispose();
    }

	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object obj) {
		if( obj instanceof IConnectionWrapper ) {
			IConnectionProvider provider = ((IConnectionWrapper)obj).getProvider();
			return provider.getName((IConnectionWrapper)obj);
		}
		if (obj instanceof DomainNode) {
			DomainNode node = (DomainNode) obj;
			return node.getDomain();
		}
		if (obj instanceof ObjectNameNode) {
			ObjectNameNode node = (ObjectNameNode) obj;
			return node.getValue();
		}
		if (obj instanceof PropertyNode) {
			PropertyNode node = (PropertyNode) obj;
			return node.getValue();
		}
		if (obj instanceof MBeanInfoWrapper) {
			MBeanInfoWrapper wrapper = (MBeanInfoWrapper) obj;
			return wrapper.getObjectName().toString();
		}
		if (obj instanceof MBeanOperationInfoWrapper) {
			MBeanOperationInfoWrapper wrapper = (MBeanOperationInfoWrapper) obj;
			return MBeanUtils.prettySignature(wrapper.getMBeanOperationInfo());
		}
		if (obj instanceof MBeanAttributeInfoWrapper) {
			MBeanAttributeInfoWrapper wrapper = (MBeanAttributeInfoWrapper) obj;
			return wrapper.getMBeanAttributeInfo().getName();
		}
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
		if( obj instanceof IConnectionWrapper ) {
			IConnectionProvider provider = ((IConnectionWrapper)obj).getProvider();
			ConnectionProviderUI ui = UIExtensionManager.getConnectionProviderUI(provider.getId());
			if( ui != null ) {
				if(!images.containsKey(ui.getId()) || images.get(ui.getId()).isDisposed())
					images.put(ui.getId(),
							ui.getImageDescriptor().createImage());
				return images.get(ui.getId());
			}
		}

		if (obj instanceof DomainNode) {
			return JMXImages.get(JMXImages.IMG_OBJS_LIBRARY);
		}
		if (obj instanceof ObjectNameNode) {
			return JMXImages.get(JMXImages.IMG_OBJS_METHOD);
		}
		if (obj instanceof PropertyNode) {
			return JMXImages.get(JMXImages.IMG_OBJS_PACKAGE);
		}
		if (obj instanceof MBeanInfoWrapper) {
			return JMXImages.get(JMXImages.IMG_OBJS_METHOD);
		}
		if (obj instanceof MBeanAttributeInfoWrapper) {
			return JMXImages.get(JMXImages.IMG_FIELD_PUBLIC);
		}
		if (obj instanceof MBeanOperationInfoWrapper) {
			return JMXImages.get(JMXImages.IMG_MISC_PUBLIC);
		}
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

}