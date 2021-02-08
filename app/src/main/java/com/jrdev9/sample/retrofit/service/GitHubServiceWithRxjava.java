package com.jrdev9.sample.retrofit.service;

import com.jrdev9.sample.retrofit.model.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

public interface GitHubServiceWithRxjava {

    /**
     * Request asynchronous with header annotation
     */
    @Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}")
    Observable<User> getInfoUser(@Path("user") String user);

    /**
     * Request synchronous
     */
    @GET("/users/{user}/followers")
    Observable<List<User>> getFollowersByUser(@Path("user") String user);

    /**
     * Request observable
     */
    @GET("/users/{user}/following")
    Observable<List<User>> getFollowingsByUser(@Path("user") String user);
}
