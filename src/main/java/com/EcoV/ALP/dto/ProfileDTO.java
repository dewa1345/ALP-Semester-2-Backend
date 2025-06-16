package com.EcoV.ALP.dto;

public class ProfileDTO {

    private String name;
    private String pfp;
    private boolean stat;
    private Long id_user; // ✅ Added user ID field

    // ====== Getters ======

    public String getName() {
        return this.name;
    }

    public String getPfp() {
        return this.pfp;
    }

    public Boolean getStat() {
        return this.stat;
    }

    public Long getId_user() {
        return this.id_user;
    }

    // ====== Setters ======

    public void setName(String name) {
        this.name = name;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public void setStat(Boolean stat) {
        this.stat = stat;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }
}
