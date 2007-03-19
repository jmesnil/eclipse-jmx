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

    public static String AttributeControlFactory_updateButtonTitle;

    public static String AttributeDetails_title;

    public static String AttributeDetailsSection_errorTitle;

    public static String AttributesPage_title;

    public static String AttributesSection_title;

    public static String className;

    public static String CollapseAllAction_text;

    public static String ConnectionSelectionDialog_error;

    public static String ConnectionSelectionDialog_host;

    public static String ConnectionSelectionDialog_invalid_host;

    public static String ConnectionSelectionDialog_invalid_port;

    public static String ConnectionSelectionDialog_invalid_url;

    public static String ConnectionSelectionDialog_port;

    public static String ConnectionSelectionDialog_title;

    public static String description;

    public static String domain;

    public static String general;

    public static String horizontal;

    public static String impact;

    public static String InfoPage_notificationsSectionTitle;

    public static String InfoPage_title;

    public static String InvocationResultDialog_title;

    public static String key;

    public static String LayoutActionGroup_flat;

    public static String LayoutActionGroup_hierarchical;

    public static String LayoutActionGroup_menu;

    public static String MBeanAttributeValue_Warning;

    public static String MBeanExplorer_linkWithEditorActionText;

    public static String MBeanServerConnectAction_connectionFailure;

    public static String MBeanServerConnectAction_error;

    public static String MBeanServerConnectAction_text;

    public static String MBeanServerConnectDialog_advancedTab;

    public static String MBeanServerConnectDialog_password;

    public static String MBeanServerConnectDialog_simpleTab;

    public static String MBeanServerConnectDialog_userName;

    public static String MBeanServerDisconnectAction_text;

    public static String name;

    public static String NotificationsPage_title;

    public static String objectName;

    public static String ObjectNameNode_error;

    public static String OpenMBeanAction_description;

    public static String OpenMBeanAction_dialogDescription;

    public static String OpenMBeanAction_dialogTitle;

    public static String OpenMBeanAction_text;

    public static String OpenMBeanAction_tooltip;

    public static String OperationDetails_invocationError;

    public static String OperationDetails_invocationResult;

    public static String OperationDetails_invocationSuccess;

    public static String OperationDetails_title;

    public static String OperationsPage_formText;

    public static String OperationsPage_title;

    public static String OperationsSection_title;

    public static String parameters;

    public static String permission;

    public static String readable;

    public static String readOnly;

    public static String readWrite;

    public static String returnType;

    public static String type;

    public static String unavailable;

    public static String value;

    public static String vertical;

    public static String writable;

    public static String writeOnly;

    private static final String BUNDLE_NAME = "net.jmesnil.jmx.ui.internal.messages"; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
