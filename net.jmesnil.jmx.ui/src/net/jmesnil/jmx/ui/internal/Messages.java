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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "net.jmesnil.jmx.ui.internal.messages"; //$NON-NLS-1$

    public static String AttributeControlFactory_updateButtonTitle;

    public static String AttributeDetailsSection_errorTitle;

    public static String AttributeDetailsSection_permission;

    public static String className;

    public static String CollapseAllAction_text;

    public static String ConnectionSelectionDialog_error;

    public static String ConnectionSelectionDialog_host;

    public static String ConnectionSelectionDialog_invalid_host;

    public static String ConnectionSelectionDialog_invalid_port;

    public static String ConnectionSelectionDialog_invalid_url;

    public static String ConnectionSelectionDialog_port;

    public static String ConnectionSelectionDialog_title;

    public static String details;

    public static String description;

    public static String domain;

    public static String general;

    public static String InvocationResultDialog_title;

    public static String key;

    public static String impact;

    public static String LayoutActionGroup_flat;

    public static String LayoutActionGroup_hierarchical;

    public static String LayoutActionGroup_menu;

    public static String MBeanServerConnectAction_text;

    public static String MBeanServerConnectDialog_password;

    public static String MBeanServerConnectDialog_userName;

    public static String MBeanServerDisconnectAction_text;

    public static String ObjectNameNode_error;

    public static String unavailable;

    public static String MBeanAttributeValue_Warning;

    public static String MBeanInfoView_attrSectionDesc;

    public static String MBeanInfoView_attrSectionTitle;

    public static String MBeanInfoView_infoSectionDesc;

    public static String MBeanInfoView_infoSectionTitle;

    public static String MBeanInfoView_javaClass;

    public static String MBeanInfoView_opSectionDesct;

    public static String MBeanInfoView_opSectionTitle;

    public static String MBeanInfoView_summary;

    public static String MBeanOperationInvocationView_error;

    public static String MBeanOperationInvocationView_invocationTitle;

    public static String MBeanOperationInvocationView_result;

    public static String MBeanOperationInvocationView_success;

    public static String MBeanServerConnectAction_connectionFailure;

    public static String MBeanServerConnectAction_error;

    public static String MBeanServerConnectDialog_advancedTab;

    public static String MBeanServerConnectDialog_simpleTab;

    public static String name;

    public static String objectName;

    public static String parameters;

    public static String readable;

    public static String readOnly;

    public static String readWrite;

    public static String returnType;

    public static String type;

    public static String value;

    public static String writable;

    public static String writeOnly;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
