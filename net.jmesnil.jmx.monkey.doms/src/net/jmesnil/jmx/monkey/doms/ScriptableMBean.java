package net.jmesnil.jmx.monkey.doms;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableMBean extends ScriptableObject {

	private static final long serialVersionUID = -8217828175187932745L;

	private ObjectName on;

	private MBeanServerConnection mbsc;

	public ScriptableMBean() {
	}

	public ScriptableMBean(ObjectName on, MBeanServerConnection mbsc) {
		this.on = on;
		this.mbsc = mbsc;

	}

	public ObjectName getObjectName() {
		return on;
	}

	public String getClassName() {
		return "ScriptableMBean";
	}

	public boolean has(String name, Scriptable arg1) {
		try {
			MBeanInfo info = mbsc.getMBeanInfo(on);
			MBeanAttributeInfo[] attributes = info.getAttributes();
			for (int i = 0; i < attributes.length; i++) {
				MBeanAttributeInfo attribute = attributes[i];
				if (name.equals(attribute.getName())) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public Object get(String name, Scriptable arg1) {
		try {
			return mbsc.getAttribute(on, name);
		} catch (Exception e) {
			return null;
		}
	}

	public void put(String name, Scriptable arg1, Object value) {
		try {
			mbsc.setAttribute(on, new Attribute(name, value));
		} catch (Exception e) {
		}
	}

	public Object getDefaultValue(Class arg0) {
		return "[object ScriptableMBean]";
	}
}
