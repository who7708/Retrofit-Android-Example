package com.jrdev9.sample.retrofit.service;

import com.jrdev9.sample.retrofit.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface GitHubServiceWithRetrofit1 {

    /**
     * Request asynchronous with header annotation
     */
    @Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}")
    void getInfoUser(@Path("user") String user, Callback<User> infoUserCallback);

    /**
     * Request synchronous
     */
    @GET("/users/{user}/followers")
    void getFollowersByUser(@Path("user") String user, Callback<List<User>> usersCallback);

    /**
     * Request observable
     */
    @GET("/users/{user}/following")
    void getFollowingsByUser(@Path("user") String user, Callback<List<User>> usersCallback);
}
