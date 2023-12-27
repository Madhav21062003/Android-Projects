package com.example.chatit.models;

public class PostMember {
    String name, url, postUri, time, uid, type, desc;

    public PostMember(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static class RequestMember {
        String name;
        String url;
        String profession;
        String bio;
        String privacy;
        String email;
        String followers;
        String website;
        String userid;

        public  RequestMember(){

        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFollowers() {
            return followers;
        }

        public void setFollowers(String followers) {
            this.followers = followers;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
