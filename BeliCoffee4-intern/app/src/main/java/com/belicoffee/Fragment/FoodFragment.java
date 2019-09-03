package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belicoffee.Manager.DatabaseHelper;
import com.belicoffee.Model.Drink;
import com.belicoffee.Model.Food;
import com.belicoffee.Adapter.FoodAdapter;
import com.belicoffee.Manager.IShippingView;
import com.belicoffee.R;
import com.belicoffee.Manager.ShippingManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment implements IShippingView {
    ArrayList<Food> foods;
    RecyclerView recyclerView;
    ShippingManager shippingManager;
    DatabaseHelper databaseHelper;
    FoodAdapter foodAdapter;

    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_shipping, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shippingManager.setListener(this);
        shippingManager.requestDatabase();
    }

    @Override
    public void onPause() {
        super.onPause();
        shippingManager.removeListener(this);
    }

    @Override
    public void updateRecyclerView(ArrayList<Drink> drinks, ArrayList<Food> foods) {
        this.foods = foods;
        foodAdapter.updateData(foods);
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_drinkfood_shipping);
        foods = new ArrayList<>();
        shippingManager = ShippingManager.getInstance(getContext());
        databaseHelper = DatabaseHelper.getInstance(getContext());
        foodAdapter = new FoodAdapter(getActivity(), foods);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }
}
