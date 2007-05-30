package net.jmesnil.jmx.ui.test.interactive;

public interface NotifEmitterMBean {
    void startEmit();

    void stopEmit();
    
    boolean isEmmitting();
}
