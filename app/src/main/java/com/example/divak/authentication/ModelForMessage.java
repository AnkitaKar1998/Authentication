package com.example.divak.authentication;

import java.util.ArrayList;

public class ModelForMessage {

    ArrayList<ModelForSingleMsg> msg;
    String teacherId,teacherName;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public ArrayList<ModelForSingleMsg> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<ModelForSingleMsg> msg) {
        this.msg = msg;
    }

}
