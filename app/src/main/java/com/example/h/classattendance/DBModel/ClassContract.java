package com.example.h.classattendance.DBModel;

import android.provider.BaseColumns;

public final class ClassContract {

    public ClassContract () {

    }

    /**
     * Inner class that defines the table contents
     *
     */

    public static class ClassEntry implements BaseColumns {
        public static final String LOGIN_TABLE_NAME = "LoginDetails";
        public static final String COLUMN_STAFF_ID = "staffId";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_LOGIN_TIMESTAMP = "timestamp";
        public static final String STUD_TABLE_NAME = "StudentDetails";
        public static final String COLUMN_COURSE = "course";
        public static final String COLUMN_MATRIC = "matricNo";
        public static final String COLUMN_SCAN_TIMESTAMP = "timestamp";

    }

    private int staffId;
    private String password;
    private String timestamp;

    public ClassContract (int staffId, String password, String timestamp) {
        this.staffId = staffId;
        this.password = password;
        this.timestamp = timestamp;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
