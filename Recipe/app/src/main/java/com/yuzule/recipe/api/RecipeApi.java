package com.yuzule.recipe.api;

import com.yuzule.recipe.model.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("search")
    Observable<Response> getRecipes(@Query("keyword") String keyword, @Query("num") int num, @Query("appkey") String appkey);

}
