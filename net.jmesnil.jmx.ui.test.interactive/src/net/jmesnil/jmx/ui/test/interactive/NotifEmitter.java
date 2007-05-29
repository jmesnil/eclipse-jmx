package net.jmesnil.jmx.ui.test.interactive;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.StandardMBean;

public class NotifEmitter extends StandardMBean implements NotifEmitterMBean,
        NotificationEmitter {

    private boolean emitNotification = false;

    private long sequence = 0;

    private NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();

    public NotifEmitter() throws NotCompliantMBeanException {
        super(NotifEmitterMBean.class);
        Thread emitter = new Thread() {
            public void run() {
                while (true) {
                    if (emitNotification) {
                        Notification notification = new Notification("notif",
                                this, sequence, "this is message " + sequence);
                        notification
                                .setSource("net.jmesnil.test:type=NotifEmitter");
                        broadcaster.sendNotification(notification);
                    }
                    sequence++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        emitter.setDaemon(true);
        emitter.start();
    }

    public void startEmit() {
        emitNotification = true;
    }

    public void stopEmit() {
        emitNotification = false;
    }

    public boolean isEmmitting() {
        return emitNotification;
    }

    public void removeNotificationListener(NotificationListener listener,
            NotificationFilter filter, Object handback)
            throws ListenerNotFoundException {
        broadcaster.removeNotificationListener(listener, filter, handback);
    }

    public void addNotificationListener(NotificationListener listener,
            NotificationFilter filter, Object handback)
            throws IllegalArgumentException {
        broadcaster.addNotificationListener(listener, filter, handback);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return broadcaster.getNotificationInfo();
    }

    public void removeNotificationListener(NotificationListener listener)
            throws ListenerNotFoundException {
        broadcaster.removeNotificationListener(listener);
    }

}
