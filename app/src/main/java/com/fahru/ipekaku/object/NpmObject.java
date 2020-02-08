package com.fahru.ipekaku.object;

public class NpmObject {
    private String fullnpm;
    private String generation;
    private String name;
    private String npm;
    private String major;
    private String uid;
    private String email;

    public NpmObject(){
        //needed
    }

    public NpmObject(String fullnpm, String generation, String name, String npm, String major, String uid, String email) {
        this.fullnpm = fullnpm;
        this.generation = generation;
        this.name = name;
        this.npm = npm;
        this.major = major;
        this.uid = uid;
        this.email = email;
    }

    public String getFullnpm() {
        return fullnpm;
    }

    public String getGeneration() {
        return generation;
    }

    public String getName() {
        return name;
    }

    public String getNpm() {
        return npm;
    }

    public String getMajor() {
        return major;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}
