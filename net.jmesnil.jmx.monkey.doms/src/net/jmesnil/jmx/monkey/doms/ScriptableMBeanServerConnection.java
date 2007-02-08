package net.jmesnil.jmx.monkey.doms;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class ScriptableMBeanServerConnection {
	private MBeanServerConnection mbsc;

	public ScriptableMBeanServerConnection(MBeanServerConnection mbsc) {
		this.mbsc = mbsc;
	}

	public MBeanServerConnection getObject() {
		return mbsc;
	}

	public Object invoke(ScriptableMBean mbean, String operationName,
			Object[] params, String[] signature) throws Exception {
		return mbsc.invoke(mbean.getObjectName(), operationName, params,
				signature);
	}

	public ScriptableMBean getMBean(String objectNameStr) throws Exception {
		ObjectName on = new ObjectName(objectNameStr);
		return new ScriptableMBean(on, mbsc);
	}
}
