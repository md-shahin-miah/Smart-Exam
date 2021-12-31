package com.crux.qxm.networkLayer.search.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("search") // end point
    Single<List<String>> getPosts(@Query("post_title") String query);


}
