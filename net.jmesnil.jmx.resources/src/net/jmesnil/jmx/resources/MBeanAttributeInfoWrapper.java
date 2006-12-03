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
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.eclipse.core.runtime.Assert;

public class MBeanAttributeInfoWrapper extends MBeanFeatureInfoWrapper {

    private MBeanAttributeInfo info;

    public MBeanAttributeInfoWrapper(MBeanAttributeInfo info, ObjectName on,
            MBeanServerConnection mbsc) {
        super(on, mbsc);
        Assert.isNotNull(info);
        this.info = info;
    }

    public MBeanAttributeInfo getMBeanAttributeInfo() {
        return info;
    }
}
