package com.jrdev9.sample.retrofit.service;

import com.jrdev9.sample.retrofit.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * retrofit1 实现
 */
public interface GitHubServiceWithRetrofit2 {

    /**
     * Request asynchronous with header annotation
     */
    @Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}")
    Call<User> getInfoUser(@Path("user") String user);

    /**
     * Request synchronous
     */
    @GET("/users/{user}/followers")
    Call<List<User>> getFollowersByUser(@Path("user") String user);

    /**
     * Request observable
     */
    @GET("/users/{user}/following")
    Call<List<User>> getFollowingsByUser(@Path("user") String user);
}
