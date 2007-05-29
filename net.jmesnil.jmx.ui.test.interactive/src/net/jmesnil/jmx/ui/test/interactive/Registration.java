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
package net.jmesnil.jmx.ui.test.interactive;

import java.lang.management.ManagementFactory;

import javax.management.ObjectName;
import javax.management.StandardMBean;

public class Registration extends StandardMBean implements RegistrationMBean {

    private ObjectName tempOn;

    public Registration() throws Exception {
        super(RegistrationMBean.class);
        tempOn = ObjectName.getInstance("net.jmesnil.test:Type=Temporary");
    }

    public void registerTemporary() throws Exception {
        Temporary temp = new Temporary();
        ManagementFactory.getPlatformMBeanServer().registerMBean(temp, tempOn);
    }

    public void unregisterTemporary() throws Exception {
        ManagementFactory.getPlatformMBeanServer().unregisterMBean(tempOn);
    }

}
