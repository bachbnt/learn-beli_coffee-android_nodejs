package com.belicoffee.Manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.belicoffee.Model.Drink;
import com.belicoffee.Model.Food;
import com.belicoffee.Model.ProductList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShippingManager {
    Context context;
    DatabaseHelper databaseHelper;
    ArrayList<IShippingView> observer;
    ArrayList<Drink> drinks;
    ArrayList<Food> foods;

    static ShippingManager shippingManager;

    private ShippingManager(Context context) {
        this.context = context;
        this.databaseHelper = DatabaseHelper.getInstance(context);
        observer = new ArrayList<>();
        drinks = new ArrayList<>();
        foods = new ArrayList<>();
    }

    public void getResponse() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIJson.urlDrink).addConverterFactory(GsonConverterFactory.create()).build();
        APIJson apiJson = retrofit.create(APIJson.class);
        Call<ProductList> call = apiJson.getProducts();
        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ProductList productList = response.body();
                        drinks = productList.drinks;
                        if (drinks != null) {
                            databaseHelper.deleteDrinkDatabase();
                            for (int i = 0; i < drinks.size(); i++) {
                                databaseHelper.insertDrinkDatabase(drinks.get(i));
                            }
                        }
                        requestDatabase();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
            }
        });
    }

    public void setListener(IShippingView iShippingView) {
        this.observer.add(iShippingView);
    }

    public void removeListener(IShippingView iShippingView) {
        this.observer.remove(iShippingView);
    }

    public static ShippingManager getInstance(Context context) {
        if (shippingManager == null)
            shippingManager = new ShippingManager(context);
        return shippingManager;
    }

    public void requestDatabase() {
        RequestDatabaseTask databaseTask = new RequestDatabaseTask();
        databaseTask.execute();
    }

    public class RequestDatabaseTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            drinks = DatabaseHelper.getInstance(context).loadDrinkDatabase();
            foods = DatabaseHelper.getInstance(context).loadFoodDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("TAG", "onPostExecute");
            if (drinks != null || foods != null)
                for (IShippingView iShippingView : observer) {
                    iShippingView.updateRecyclerView(drinks, foods);
                }
        }
    }

//    public ArrayList<Drink> parsonJson(String strJson) {
//        ArrayList<Drink> drinks = new ArrayList<>();
//        try {
//            JSONObject jsonObj = new JSONObject(strJson);
//            JSONArray jsonArr = jsonObj.getJSONArray("coffee");
//            for (int i = 0; i < jsonArr.length(); i++) {
//                Drink drink = new Drink();
//                JSONObject object = jsonArr.getJSONObject(i);
//                drink.setId(object.getInt("id"));
//                drink.setName(object.getString("name"));
//                drink.setPrice(object.getDouble("price"));
//                drink.setImage(object.getString("image"));
//                drinks.add(drink);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return drinks;
//    }

    //send a request to get result
//    public void requestJson(int type) {
//        RequestJsonTask requestJsonTask;
//        switch (type) {
//            case 1:
//                requestJsonTask = new RequestJsonTask(1);
//                requestJsonTask.execute(urlDrink);
//                break;
//            case 2:
//                requestJsonTask = new RequestJsonTask(2);
//                requestJsonTask.execute(urlFood);
//                break;
//        }
//    }

//    public String requestUrl(String stringUrl) {
//        final String REQUEST_METHOD = "GET";
//        final int READ_TIMEOUT = 5000;
//        final int CONNECTION_TIMEOUT = 5000;
//        String result = null;
//        String inputLine;
//        try {
//            //Create a URL object holding our url
//            URL myUrl = new URL(stringUrl);
//            //Create a connection
//            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
//            //Set methods and timeouts
//            connection.setRequestMethod(REQUEST_METHOD);
//            connection.setReadTimeout(READ_TIMEOUT);
//            connection.setConnectTimeout(CONNECTION_TIMEOUT);
//            //Connect to our url
//            connection.joinSocket();
//            //Create a new InputStreamReader
//            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//            //Create a new buffered reader and String Builder
//            BufferedReader reader = new BufferedReader(streamReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            //Check if the line we are reading is not null
//            while ((inputLine = reader.readLine()) != null) {
//                stringBuilder.append(inputLine);
//            }
//            //Close our InputStream and Buffered reader
//            reader.close();
//            streamReader.close();
//            //Set our result equal to our stringBuilder
//            result = stringBuilder.toString();
//        } catch (Exception e) {
//            Log.e("TAG", "RequestKeyTask.doInBackground(): Fail with exception = " + e.toString());
//            return null;
//        }
//        return result;
//    }
//
//
//    //handl json data
//    public ArrayList<Drink> parseJsonDrink(String result) {
//        ArrayList<Drink> drinks = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(result);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                int id = jsonObject.getInt("id");
//                String name = jsonObject.getString("name");
//                Double price = jsonObject.getDouble("price");
//                String url = jsonObject.getString("url");
//                Drink drink = new Drink(id, name, price, url);
//                drinks.add(drink);
//            }
//        } catch (JSONException e) {
//            Log.e("TAG", "dataHandler():" + e.toString());
//            return null;
//        }
//        return drinks;
//    }

//    public ArrayList<Food> parseJsonFood(String result) {
//        if (result == null)
//            return null;
//        ArrayList<Food> foods = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(result);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                int id = jsonObject.getInt("id");
//                String name = jsonObject.getString("name");
//                Double price = jsonObject.getDouble("price");
//                String image = jsonObject.getString("url");
//                Food food = new Food(id, name, price, image);
//                foods.add(food);
//            }
//        } catch (
//                JSONException e) {
//            Log.e("TAG", "dataHandler():" + e.toString());
//            return null;
//        }
//        return foods;
//    }


//    public class RequestJsonTask extends AsyncTask<String, Void, Void> {
//        int type;
//
//        public RequestJsonTask(int type) {
//            this.type = type;
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            String stringUrl = params[0];
//            String result = requestUrl(stringUrl);
//            if (result != null) {
//                switch (type) {
//                    case 1:
//                        drinks = parseJsonDrink(result);
//                        URL url = null;
//                        if (drinks != null) {
//                            for (int i = 0; i < drinks.size(); i++) {
//                                try {
//                                    url = new URL((drinks.get(i)).getUrl());
//                                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                    Bitmap resizedBitmap = BitmapUtility.getResizedBitmap(bitmap, 200, 200);
//                                    (drinks.get(i)).setImage(BitmapUtility.convertBitmap2Byte(resizedBitmap));
//                                } catch (Exception e) {
//                                    Log.i("tag", "doInBackground");
//                                }
//                            }
//                        }
//                    case 2:
//                        foods = parseJsonFood(result);
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Log.d("TAG", "onPostExecute");
//
//            if (drinks != null) {
//                databaseHelper.deleteDrinkDatabase();
//                for (int i = 0; i < drinks.size(); i++) {
//                    databaseHelper.insertDrinkDatabase(drinks.get(i));
//                }
//            }
//            if (foods != null) {
//                databaseHelper.deleteFoodDatabase();
//                for (int i = 0; i < foods.size(); i++) {
//                    databaseHelper.insertFoodDatabase(foods.get(i));
//                }
//            }
//            shippingManager.requestDatabase();
//        }
//    }
}
