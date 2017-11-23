package vn.vnpt.ansv.bts.notification;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ANSV on 11/23/2017.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}