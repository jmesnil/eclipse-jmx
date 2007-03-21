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

public class MBeanAttributeInfoWrapper extends MBeanFeatureInfoWrapper {

    private MBeanAttributeInfo info;

    public MBeanAttributeInfoWrapper(MBeanAttributeInfo attrInfo,
            MBeanInfoWrapper wrapper) {
        super(wrapper);
        this.info = attrInfo;
    }

    public MBeanAttributeInfo getMBeanAttributeInfo() {
        return info;
    }
    
    public Object getValue() throws Exception {
           MBeanServerConnection mbsc = getMBeanServerConnection();
        return mbsc.getAttribute(getObjectName(),  info.getName());
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((info == null) ? 0 : info.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MBeanAttributeInfoWrapper))
            return false;
        final MBeanAttributeInfoWrapper other = (MBeanAttributeInfoWrapper) obj;
        if (info == null) {
            if (other.info != null)
                return false;
        } else if (!info.equals(other.info))
            return false;
        return true;
    }
}
