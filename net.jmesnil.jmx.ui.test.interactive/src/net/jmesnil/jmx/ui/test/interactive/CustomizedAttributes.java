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

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class CustomizedAttributes extends StandardMBean implements CustomizedAttributesMBean {

    public CustomizedAttributes() throws NotCompliantMBeanException {
        super(CustomizedAttributesMBean.class);
    }

    public String getGreenString() {
        return "must be displayed in green in details section"; //$NON-NLS-1$
    }

    public String getRedString() {
        return "must be displayed in red in details section"; //$NON-NLS-1$
    }

    public String getUnmodifiedString() {
        return "must be displayed in normal color (black) in details section"; //$NON-NLS-1$
    }

}
