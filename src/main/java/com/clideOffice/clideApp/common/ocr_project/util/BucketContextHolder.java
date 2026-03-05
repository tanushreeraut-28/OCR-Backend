package com.clideOffice.clideApp.common.ocr_project.util;

public class BucketContextHolder {

    public static String getBucketName(String database) {

        if (database == null || database.isBlank()) {
            throw new RuntimeException("Database context is not set");
        }

        return "clide-" + database;
    }
}