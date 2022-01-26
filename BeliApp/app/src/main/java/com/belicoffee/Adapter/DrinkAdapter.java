package com.belicoffee.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.belicoffee.Dialog.ShippingDialog;
import com.belicoffee.Model.Drink;
import com.belicoffee.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {
    Context context;
    ArrayList<Drink> drinks;
    OnItemClickListener onItemClickListener;

    public DrinkAdapter(Context context,ArrayList<Drink> drinks, OnItemClickListener listener) {
        this.context=context;
        this.drinks = drinks;
        this.onItemClickListener=listener;
    }

    public void updateData(ArrayList<Drink> drinks) {
        this.drinks = drinks;
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
        final Drink drink = drinks.get(i); //data from database
        Glide.with(context).load(drink.getImage()).override(200, 200).centerCrop().into((viewHolder).ivImage);
        viewHolder.tvName.setText(drink.getName());
        viewHolder.tvPrice.setText(ShippingDialog.formatMoney(drink.getPrice()));
        viewHolder.llDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(drink);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvPrice;
        LinearLayout llDrink;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image_drinkfood);
            tvName = itemView.findViewById(R.id.tv_name_drinkfood);
            tvPrice = itemView.findViewById(R.id.tv_price_drinkfood);
            llDrink=itemView.findViewById(R.id.ll_drink);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Drink drink);
    }
}

