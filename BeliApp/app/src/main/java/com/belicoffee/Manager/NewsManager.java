package com.belicoffee.Manager;

import android.util.Log;

import com.belicoffee.Model.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsManager {
    static NewsManager newsManager;
    APINews APINews;
    RequestListener requestListener;

    private NewsManager() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://belicoffeeserver.herokuapp.com/").addConverterFactory(GsonConverterFactory.create()).build();
        APINews = retrofit.create(APINews.class);
    }

    public void setListenner(RequestListener listenner) {
        requestListener = listenner;
    }

    public static NewsManager getInstance() {
        if (newsManager == null)
            newsManager = new NewsManager();
        return newsManager;
    }

    public void getNews() {
        Call<ArrayList<News>> call = APINews.getNews();
        call.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                ArrayList<News> newsArrayList = response.body();
                requestListener.onComplete(newsArrayList);
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                Log.i("tag", "onFailure" + t);
            }
        });
    }

    public void createNews(String title, String content, String image) {
        Call<News> call = APINews.createNews(title, content, image);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();
                Log.i("tag", "createNews onResponse "+news.getImage());
                getNews();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.i("tag", "createNews onFailure");
            }
        });
    }

    public void updateNews(String id, String title, String content, String image) {
        Call<News> call = APINews.updateNews(id, title, content, image);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();
                Log.i("tag", "updateNews onResponse " + news);
                getNews();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.i("tag", "updateNews onFailure " + t);

            }
        });
    }

    public void deleteNews(String id) {
        Call<News> call = APINews.deleteNews(id);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();
                Log.i("tag", "deleteNews onResponse " + news.getId());
                getNews();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.i("tag", "deleteNews onFailure " + t);
            }
        });
    }

    public interface RequestListener {
        void onComplete(ArrayList<News> newsArrayList);
    }
}
