package com.belicoffee.Manager;

import android.view.View;

import com.belicoffee.Model.Drink;
import com.belicoffee.Model.Food;

import java.util.ArrayList;

public interface IShippingView {
    void updateRecyclerView(ArrayList<Drink> drinks, ArrayList<Food> foods);
    void initView(View view);
}
