package com.auth0.samples.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ayush on 19/3/18.
 */

public class GitHubApi {

    public static GitHubService createGitHubService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com");
        return builder.build().create(GitHubService.class);
    }
}