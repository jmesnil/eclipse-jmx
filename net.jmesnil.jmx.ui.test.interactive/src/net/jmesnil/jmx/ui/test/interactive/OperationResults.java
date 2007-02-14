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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class OperationResults extends StandardMBean implements
        OperationResultsMBean {

    public OperationResults() throws NotCompliantMBeanException {
        super(OperationResultsMBean.class);
    }
    
    public int[] intsOperation() {
        return new int[] {-3, -2, -1, 0, 1, 2, 3};
    }

    public String stringOperation() {
        return "operation returned a String"; //$NON-NLS-1$
    }

    public void voidOperation() {
        // do nothing
    }
    
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public Collection collectionOperation() {
        Collection<String> coll = new ArrayList<String>();
        coll.add("first"); //$NON-NLS-1$
        coll.add("second"); //$NON-NLS-1$
        coll.add("third"); //$NON-NLS-1$
        coll.add("fourth"); //$NON-NLS-1$
        return coll;
    }
    
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public Map mapOperation() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("first key", "first value"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put("second key", "second value"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put("third key", "third value"); //$NON-NLS-1$ //$NON-NLS-2$
        return map;
    }
}
