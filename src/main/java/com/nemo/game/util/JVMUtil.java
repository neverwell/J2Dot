package com.nemo.game.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class JVMUtil {

    public static final int fetchProcessId() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();
            return Integer.parseInt(name.substring(0, name.indexOf("@")));
        } catch (Exception e) {
            return -1;
        }
    }
}
