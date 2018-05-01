package com.example.paulnovacovici.portfolio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by paulnovacovici on 10/22/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUser {
    @JsonProperty("public_repos")
    private String public_repos;

    @JsonProperty("avatar_url")
    private String avatar_url;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("name")
    private String name;

    @JsonProperty("login")
    private String user_name;

    private String following;
    private String followers;

    public GithubUser(String public_repos, String avatar_url, String bio,String name, String user_name, String following, String followers){
        this.public_repos = public_repos;
        this.avatar_url = avatar_url;
        this.bio = bio;
        this.name = name;
        this.user_name = user_name;
        this.followers = followers;
        this.following = following;
    }

    public String getPublic_Repos(){
        return this.public_repos;
    }

    public String getUserName() {return this.user_name;}

    public String getName() {return this.name;}

    public String getBio() {return this.bio;}

    public String getAvatar_url() {return this.avatar_url;}

    public String getFollowing(){
        return this.following;
    }

    public String getFollowers(){
        return this.followers;
    }
}
