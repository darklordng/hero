package com.example.h.classattendance.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffList {
    @SerializedName("staffId")
    @Expose
    private String staffId;

    @SerializedName("password")
    @Expose
    private String password;

    public StaffList (String staffId, String password) {
        this.staffId = staffId;
        this.password = password;
    }

    public StaffList () {

    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffid(String staffId) {
        this.staffId = staffId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
