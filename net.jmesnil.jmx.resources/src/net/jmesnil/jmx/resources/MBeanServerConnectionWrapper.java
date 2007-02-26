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

import java.io.IOException;

import javax.management.MBeanServerConnection;

import org.eclipse.core.runtime.Assert;

public class MBeanServerConnectionWrapper {
    private final MBeanServerConnection mbsc;

    public MBeanServerConnectionWrapper(MBeanServerConnection mbsc) {
        Assert.isNotNull(mbsc);
        this.mbsc = mbsc;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

    public DomainWrapper[] getDomains() {
        try {
            String[] domainsStr = mbsc.getDomains();
            DomainWrapper[] domains = new DomainWrapper[domainsStr.length];
            for (int i = 0; i < domainsStr.length; i++) {
                String domainStr = domainsStr[i];
                domains[i] = new DomainWrapper(domainStr, mbsc);
            }
            return domains;
        } catch (IOException e) {
            e.printStackTrace();
            return new DomainWrapper[0];
        }
    }
}
