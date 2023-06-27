package com.yuzule.recipe.api;

import com.yuzule.recipe.model.CollectRequest;
import com.yuzule.recipe.model.Collections;
import com.yuzule.recipe.model.Information;
import com.yuzule.recipe.model.Pictures;
import com.yuzule.recipe.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("login")
    Observable<User> login(@Query("phone") String phone, @Query("password") String password);

    @POST("register")
    Observable<Information> register(@Query("user_name") String user_name, @Query("password") String password, @Query("passwordConfirm") String passwordConfirm, @Query("phone") String phone);

    @POST("modify")
    Observable<Information> modify(@Body User user);

    @GET("pictures")
    Observable<Pictures> getPictures();

    @POST("collect")
    Observable<Information> collect(@Body CollectRequest collectRequest);

    @POST("uncollect")
    Observable<Information> uncollect(@Query("user_id") int user_id, @Query("content_id") int content_id);

    @GET("collection")
    Observable<Collections> getCollections(@Query("user_id") int user_id);
}
