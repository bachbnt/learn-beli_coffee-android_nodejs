package com.belicoffee.Manager;

import com.belicoffee.Model.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APINews {
    @GET("products")
    Call<ArrayList<News>> getNews();

    @FormUrlEncoded
    @POST("products")
    Call<News> createNews(@Field("title") String title, @Field("content") String content, @Field("image") String image);

    @FormUrlEncoded
    @PUT("products/{id}")
    Call<News> updateNews(@Path("id") String id, @Field("title") String title, @Field("content") String content,@Field("image")String image);

    @DELETE("products/{id}")
    Call<News> deleteNews(@Path("id") String id);

}
