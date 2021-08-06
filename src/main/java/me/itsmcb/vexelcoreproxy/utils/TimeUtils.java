package me.itsmcb.vexelcoreproxy.utils;

public class TimeUtils {

    public static long convertDurationToMs(long startTime, long endTime) {
        return (endTime - startTime)/1000000;
    }
}
