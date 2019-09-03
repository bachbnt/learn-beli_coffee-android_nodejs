package com.belicoffee.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.belicoffee.Manager.DatabaseHelper;
import com.belicoffee.Model.Food;
import com.belicoffee.R;

import java.net.URL;
import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    Activity activity;
    ArrayList<Food> foods;

    public FoodAdapter(Activity activity, ArrayList<Food> foods) {
        this.activity = activity;
        this.foods = foods;
    }

    public void updateData(ArrayList<Food> foods){
        this.foods = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Food food = DatabaseHelper.getInstance(activity).loadFoodDatabase().get(i); //data from database
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url= null;
                try {
                    url = new URL(food.getFoodImage());
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    final Bitmap resizedBitmap=getResizedBitmap(bitmap,viewHolder.ivImage.getWidth(),viewHolder.ivImage.getHeight());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.ivImage.setImageBitmap(resizedBitmap);
                        }
                    });

                } catch (Exception e) {
                    Log.e("TAG", "onBindViewHolder" + e.toString());
                }
            }
        }).start();
        viewHolder.tvName.setText(food.getFoodName());
        viewHolder.tvPrice.setText(String.format("%.3f", food.getFoodPrice()));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image_drinkfood);
            tvName = itemView.findViewById(R.id.tv_name_drinkfood);
            tvPrice = itemView.findViewById(R.id.tv_price_drinkfood);
        }
    }
}

