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

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.eclipse.core.runtime.Assert;

public class MBeanFeatureInfoWrapper {

    private MBeanInfoWrapper parent;

    MBeanFeatureInfoWrapper(MBeanInfoWrapper parent) {
        Assert.isNotNull(parent);
        this.parent = parent;
    }

    public ObjectName getObjectName() {
        return parent.getObjectName();
    }

    public MBeanInfoWrapper getMBeanInfoWrapper() {
        return parent;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return parent.getMBeanServerConnection();
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MBeanFeatureInfoWrapper other = (MBeanFeatureInfoWrapper) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        return true;
    }

}