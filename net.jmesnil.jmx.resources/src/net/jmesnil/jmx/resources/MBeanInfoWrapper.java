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
package net.jmesnil.jmx.resources;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.eclipse.core.runtime.Assert;

public class MBeanInfoWrapper implements Comparable {
    private final ObjectName on;

    private final MBeanInfo info;

    private final MBeanServerConnection mbsc;

    public MBeanInfoWrapper(ObjectName on, MBeanInfo info,
            MBeanServerConnection mbsc) {
        Assert.isNotNull(on);
        Assert.isNotNull(info);
        Assert.isNotNull(mbsc);
        this.on = on;
        this.info = info;
        this.mbsc = mbsc;
    }

    public ObjectName getObjectName() {
        return on;
    }

    public MBeanInfo getMBeanInfo() {
        return info;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

    public MBeanAttributeInfoWrapper[] getMBeanAttributeInfoWrappers() {
        MBeanAttributeInfo[] attributes = info.getAttributes();
        MBeanAttributeInfoWrapper[] attrWrappers = new MBeanAttributeInfoWrapper[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo attrInfo = attributes[i];
            attrWrappers[i] = new MBeanAttributeInfoWrapper(attrInfo, this);
        }
        return attrWrappers;
    }

    public MBeanOperationInfoWrapper[] getMBeanOperationInfoWrappers() {
        MBeanOperationInfo[] operations = info.getOperations();
        MBeanOperationInfoWrapper[] opWrappers = new MBeanOperationInfoWrapper[operations.length];
        for (int i = 0; i < operations.length; i++) {
            MBeanOperationInfo opInfo = operations[i];
            opWrappers[i] = new MBeanOperationInfoWrapper(opInfo, this);
        }
        return opWrappers;
    }

    public MBeanNotificationInfoWrapper[] getMBeanNotificationInfoWrappers() {
        MBeanNotificationInfo[] notifications = info.getNotifications();
        MBeanNotificationInfoWrapper[] notificationWrappers = new MBeanNotificationInfoWrapper[notifications.length];
        for (int i = 0; i < notifications.length; i++) {
            MBeanNotificationInfo opInfo = notifications[i];
            notificationWrappers[i] = new MBeanNotificationInfoWrapper(opInfo,
                    on, mbsc);
        }
        return notificationWrappers;
    }

    public MBeanFeatureInfoWrapper[] getMBeanFeatureInfos() {
        MBeanAttributeInfo[] attributes = info.getAttributes();
        MBeanOperationInfo[] operations = info.getOperations();
        MBeanFeatureInfoWrapper[] o = new MBeanFeatureInfoWrapper[attributes.length
                + operations.length];
        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo attrInfo = attributes[i];
            o[i] = new MBeanAttributeInfoWrapper(attrInfo, this);
        }
        for (int i = 0; i < operations.length; i++) {
            MBeanOperationInfo opInfo = operations[i];
            o[attributes.length + i] = new MBeanOperationInfoWrapper(opInfo,
                    this);
        }
        return o;
    }

    public int compareTo(Object object) {
        if (object instanceof MBeanInfoWrapper) {
            MBeanInfoWrapper other = (MBeanInfoWrapper) object;
            return on.toString().compareTo(other.on.toString());
        }
        return 0;
    }
}
