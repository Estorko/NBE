package com.nbe.automation.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UdidAssigner {
    private static final ThreadLocal<String> threadUdid = new ThreadLocal<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void assign(List<String> udids) {
        if (threadUdid.get() == null) {
            if (udids == null || udids.isEmpty()) {
                throw new IllegalArgumentException("UDID list cannot be empty");
            }
            int index = counter.getAndIncrement() % udids.size(); // round-robin assignment
            threadUdid.set(udids.get(index));
        }
    }

    public static String getAssignedUdid() {
        return threadUdid.get();
    }

    public static void clear() {
        threadUdid.remove();
    }
}
