package net.jmesnil.jmx.ui.test.interactive;

public interface RegistrationMBean {

    void registerTemporary() throws Exception;

    void unregisterTemporary() throws Exception;
}
