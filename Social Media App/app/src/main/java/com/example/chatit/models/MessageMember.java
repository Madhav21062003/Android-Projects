package com.example.chatit.models;

public class MessageMember {

    String message;
    String time;
    String date;
    String type;
    String sendersuid;
    String receieveruid;

    public  MessageMember(){

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendersuid() {
        return sendersuid;
    }

    public void setSendersuid(String sendersuid) {
        this.sendersuid = sendersuid;
    }

    public String getReceieveruid() {
        return receieveruid;
    }

    public void setReceieveruid(String receieveruid) {
        this.receieveruid = receieveruid;
    }
}
