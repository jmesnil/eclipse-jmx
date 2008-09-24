/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    "Rob Stryker" <rob.stryker@redhat.com> - Initial implementation
 *******************************************************************************/

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

/**
 * Label Provider for the view
 */
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