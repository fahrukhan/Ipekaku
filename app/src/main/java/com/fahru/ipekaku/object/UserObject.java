package com.fahru.ipekaku.object;

public class UserObject {

    private boolean admin;
    private boolean dev;
    private String email;
    private String uid;
    private boolean km;
    private String name;
    private String npm;
    private String reqnpm;
    private String telp;
    private String url;
    private String username;
    private boolean ver;

    public UserObject(){
        //needed
    }

    public UserObject(boolean admin, boolean dev, String email, String uid, boolean km, String name, String npm, String reqnpm, String telp, String url, String username, boolean ver) {
        this.admin = admin;
        this.dev = dev;
        this.email = email;
        this.uid = uid;
        this.km = km;
        this.name = name;
        this.npm = npm;
        this.reqnpm = reqnpm;
        this.telp = telp;
        this.url = url;
        this.username = username;
        this.ver = ver;
    }
    public boolean isAdmin() {
        return admin;
    }

    public boolean isDev() {
        return dev;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public boolean isKm() {
        return km;
    }

    public String getName() {
        return name;
    }

    public String getNpm() {
        return npm;
    }

    public String getReqnpm() {
        return reqnpm;
    }

    public String getTelp() {
        return telp;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public boolean isVer() {
        return ver;
    }
}
