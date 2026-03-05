package com.clideOffice.clideApp.common.ocr_project.util;

public class DatabaseContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setDatabase(String dbKey) {
        CONTEXT.set(dbKey);
    }

    public static String getDatabase() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}