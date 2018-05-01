package com.example.paulnovacovici.portfolio;

/**
 * Created by paulnovacovici on 10/25/2017.
 */

public class RepoObject {
    private String repo_name;
    private String owner_name;
    private String description;
    private String url;

    public RepoObject(String repo_name, String owner_name, String description, String url){
        this.repo_name = repo_name;
        this.owner_name = owner_name;
        this.description = description;
        this.url = url;
    }

    public String getUrl(){
        return this.url;
    }

    public String getRepoName(){
        return repo_name;
    }

    public String getOwnerName(){
        return owner_name;
    }

    public String getDescription(){
        return description;
    }
}
