package com.example.h.classattendance.DBModel;

public class GetResponse {

    private String email;
    private String mat_no;

    public GetResponse(String email, String mat_no) {
        this.email = email;
        this.mat_no = mat_no;
    }

    public String getMat_no() {
        return mat_no;
    }

    public void setMat_no(String mat_no) {
        this.mat_no = mat_no;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
