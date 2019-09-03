package com.belicoffee.Manager;

import com.belicoffee.Model.ProductList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIJson {
    String urlDrink = "https://raw.githubusercontent.com/tonbach18598/Beli-Coffee-2/master/";

    @GET("coffee.json")
    Call<ProductList> getProducts();
}
