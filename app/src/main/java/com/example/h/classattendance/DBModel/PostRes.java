package com.example.h.classattendance.DBModel;

import java.util.List;

public class PostRes {

    List<ResultSent> res;

    public PostRes (List<ResultSent> res) {
        this.res = res;
    }

    public List<ResultSent> getRes() {
        return res;
    }

    public void setRes(List<ResultSent> res) {
        this.res = res;
    }

    public class ResultSent {
        String course;
        String time;
        String matricno;


        public ResultSent (String course, String time, String matricno) {
            this.course = course;
            this.time = time;
            this.matricno = matricno;


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


    }
}
