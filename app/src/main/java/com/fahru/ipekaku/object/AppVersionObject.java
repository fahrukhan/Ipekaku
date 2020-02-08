package com.fahru.ipekaku.object;

public class AppVersionObject {
    private String latest;
    private int should_code;
    private String update_info;
    private int ver_code;
    private String ver_name;

    public AppVersionObject(){ }

    public AppVersionObject(String latest, int should_code, String update_info, int ver_code, String ver_name) {
        this.latest = latest;
        this.should_code = should_code;
        this.update_info = update_info;
        this.ver_code = ver_code;
        this.ver_name = ver_name;
    }

    public String getLatest() {
        return latest;
    }

    public int getShould_code() {
        return should_code;
    }

    public String getUpdate_info() {
        return update_info;
    }

    public int getVer_code() {
        return ver_code;
    }

    public String getVer_name() {
        return ver_name;
    }

}
