package com.nbe.automation.config;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UdidAssigner {
    private static final ThreadLocal<String> threadUdid = new ThreadLocal<>();
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ConcurrentHashMap<Long, String> threadToUdidMap = new ConcurrentHashMap<>();

    public static void assign(List<String> udids) {
        long threadId = Thread.currentThread().getId();
        
        // Check if this thread already has a UDID assigned
        if (threadToUdidMap.containsKey(threadId)) {
            String existingUdid = threadToUdidMap.get(threadId);
            threadUdid.set(existingUdid);
            return;
        }
        
        if (udids == null || udids.isEmpty()) {
            throw new IllegalArgumentException("UDID list cannot be empty");
        }
        
        synchronized (UdidAssigner.class) {
            int index = counter.getAndIncrement() % udids.size(); // round-robin assignment
            String assignedUdid = udids.get(index);
            threadUdid.set(assignedUdid);
            threadToUdidMap.put(threadId, assignedUdid);
        }
    }

    public static String getAssignedUdid() {
        return threadUdid.get();
    }

    public static void clear() {
        long threadId = Thread.currentThread().getId();
        threadToUdidMap.remove(threadId);
        threadUdid.remove();
    }
}