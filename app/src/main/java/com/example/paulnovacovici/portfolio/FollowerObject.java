package com.example.paulnovacovici.portfolio;

/**
 * Created by paulnovacovici on 10/26/2017.
 */

public class FollowerObject {
    private String user_name;
    private String url;
    private String avatar_url;

    public FollowerObject(String user_name, String url, String avatar_url){
        this.user_name = user_name;
        this.url = url;
        this.avatar_url = avatar_url;
    }

    public String getUserName(){
        return user_name;
    }

    public String getUrl(){
        return this.url;
    }

    public String getAvatarUrl() {
        return this.avatar_url;
    }
}
