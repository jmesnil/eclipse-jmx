package net.jmesnil.jmx.monkey.doms;

import org.eclipse.eclipsemonkey.dom.IMonkeyDOMFactory;

public class JMXDOMFactory implements IMonkeyDOMFactory {

	public JMXDOMFactory() {
	}

	public Object getDOMroot() {
		return new JMXUtils();
	}

}
