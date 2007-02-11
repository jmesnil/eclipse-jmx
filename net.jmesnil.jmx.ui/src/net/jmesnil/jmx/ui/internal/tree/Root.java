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
 * 
 */
package net.jmesnil.jmx.ui.internal.tree;

import javax.management.MBeanServerConnection;

public class Root extends Node {

    private MBeanServerConnection mbsc;

    Root(MBeanServerConnection mbsc) {
        super(null);
        this.mbsc = mbsc;
    }

    @Override
    public String toString() {
        return "Root"; //$NON-NLS-1$
    }

    public int compareTo(Object o) {
        return 0;
    }

    MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

}
