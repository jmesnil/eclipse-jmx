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

package net.jmesnil.jmx.ui.test.interactive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class ComplexType extends StandardMBean implements ComplexTypeMBean
{

  public ComplexType() throws NotCompliantMBeanException
  {
    super(ComplexTypeMBean.class);
  }
  
  public Map getMap()
  {
    return getSystemProperties();
  }
  
  public Collection getCollection()
  {
    Collection<String> coll = new ArrayList<String>();
    coll.add("first");
    coll.add("second");
    coll.add("third");
    coll.add("fourth");
    return coll;
  }
  
  public Properties getSystemProperties()
  {
    Properties props = System.getProperties();
    props.list(System.out);
    return props;
  }

}
