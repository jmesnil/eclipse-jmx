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

public class PropertyNode extends Node {
	private String key;

	private String value;

	PropertyNode(Node parent, String key, String value) {
		super(parent);
		this.key = key;
		this.value = value;
	}

	public String getKey() {
	    return key;
	}
	
	public String getValue() {
	    return value;
	}
	
	@Override
	public String toString() {
		return "PropertyNode[" + key + "=" + value + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public int compareTo(Object o) {
		PropertyNode other = (PropertyNode) o;
		if (key.equals(other.key)) {
		    return value.compareTo(other.value);
		}
		return key.compareTo(other.key);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyNode other = (PropertyNode) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
