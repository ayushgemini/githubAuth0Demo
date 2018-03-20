package com.auth0.samples.Api;

import com.auth0.samples.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ayush on 19/3/18.
 */

public interface GitHubService {
    @GET("/users")
    Call<List<User>> getUser(@Query("since") int since, @Query("per_page") int perPage);
}
