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
package net.jmesnil.jmx.resources;

import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.eclipse.core.runtime.Assert;

public class MBeanNotificationInfoWrapper {

    private MBeanNotificationInfo info;

    private MBeanServerConnection mbsc;

    private ObjectName on;

    public MBeanNotificationInfoWrapper(MBeanNotificationInfo info,
            ObjectName on, MBeanServerConnection mbsc) {
        Assert.isNotNull(info);
        Assert.isNotNull(on);
        Assert.isNotNull(mbsc);
        this.info = info;
        this.on = on;
        this.mbsc = mbsc;
    }

    public MBeanNotificationInfo getMBeanNotificationInfo() {
        return info;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

    public ObjectName getObjectName() {
        return on;
    }

}