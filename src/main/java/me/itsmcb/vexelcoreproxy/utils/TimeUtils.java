package me.itsmcb.vexelcoreproxy.utils;

public class TimeUtils {

    public static String convertDurationToMs(long startTime, long endTime) {
        return (endTime - startTime)/1000000 + "ms";
    }
}
