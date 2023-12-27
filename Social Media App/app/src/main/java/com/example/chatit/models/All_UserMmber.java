package com.example.chatit.models;

public class All_UserMmber {

    String name,  uid, prof, url, web;

    public All_UserMmber(){

    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

 /*here we store data in both Firebase RealtimeDatabase and FirebaseFireStore both
                because when we use Firestore we can retrive data in our app when you are offline in the application
             */