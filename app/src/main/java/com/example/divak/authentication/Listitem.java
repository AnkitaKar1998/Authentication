package com.example.divak.authentication;

public class Listitem {
    private String head;
    private String desc;

    public Listitem(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
