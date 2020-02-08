package com.fahru.ipekaku.object;

import com.google.firebase.Timestamp;

public class AnnouncementObject{
    private String id;
    private String title;
    private String msg;
    private int img;
    private String author;
    private Timestamp time;

    public AnnouncementObject(){}

    public AnnouncementObject(String id, String title, String msg, int img, String author, Timestamp time) {
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.img = img;
        this.author = author;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public int getImg() {
        return img;
    }

    public String getAuthor() {
        return author;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
