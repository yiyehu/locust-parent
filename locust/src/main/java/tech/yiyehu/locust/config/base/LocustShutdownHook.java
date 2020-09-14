package tech.yiyehu.locust.config.base;

import java.util.concurrent.atomic.AtomicBoolean;

public class LocustShutdownHook extends Thread{
    private static final LocustShutdownHook locustShutdownHook = new LocustShutdownHook("LocustShutdownHook");

    public static LocustShutdownHook getLocustShutdownHook() {
        return locustShutdownHook;
    }

    private final AtomicBoolean destroyed;

    public LocustShutdownHook(String name) {
        super(name);
        this.destroyed = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        destroyAll();
    }

    public void destroyAll() {
        if (!destroyed.compareAndSet(false, true)) {
            return;
        }

    }
}
