package com.EcoV.ALP.dto;

public class ProfileDTO {
    private String name;
    private String pfp;
    private boolean stat;
    public String getName(){
        return this.name;
    }
    public String getPfp(){
        return this.pfp;
    }
    public Boolean getStat(){
        return this.stat;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPfp(String pfp) {
    this.pfp = pfp;
    }
    public void setStat(Boolean stat){
        this.stat=stat;
    }
}
