package com.example.h.classattendance.DBModel;

public class Students {

    String course;
    String time;
    String matricno;

    public Students () {

    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMatricno() {
        return matricno;
    }

    public void setMatricno(String matricno) {
        this.matricno = matricno;
    }

    public Students (String course, String timestamp, String matricno) {
        this.course = course;
        this.time = timestamp;
        this.matricno = matricno;


    }
}
