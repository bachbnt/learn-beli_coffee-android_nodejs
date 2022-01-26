package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belicoffee.Adapter.DrinkAdapter;
import com.belicoffee.Dialog.ShippingDialog;
import com.belicoffee.Manager.DatabaseHelper;
import com.belicoffee.Manager.IShippingView;
import com.belicoffee.Manager.ShippingManager;
import com.belicoffee.Model.Drink;
import com.belicoffee.Model.Food;
import com.belicoffee.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkFragment extends Fragment implements IShippingView, DrinkAdapter.OnItemClickListener {
    ArrayList<Drink> drinks;
    RecyclerView recyclerView;
    ShippingManager shippingManager;
    DatabaseHelper databaseHelper;
    DrinkAdapter drinkAdapter;

    public DrinkFragment() {
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
        this.drinks = drinks;
        drinkAdapter.updateData(drinks);
        drinkAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_drinkfood_shipping);
        drinks = new ArrayList<>();
        shippingManager = ShippingManager.getInstance(getContext());
        databaseHelper = DatabaseHelper.getInstance(getContext());
        drinkAdapter = new DrinkAdapter(getActivity(), drinks, this);
        recyclerView.setAdapter(drinkAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void onItemClick(Drink drink) {
        showShippingDialog(drink.getName(), drink.getPrice(),drink.getImage());
    }

    private void showShippingDialog(String name, double price,String image) {
        FragmentManager fragmentManager=getFragmentManager();
        ShippingDialog shippingDialog=new ShippingDialog(name,price,image);
        shippingDialog.show(fragmentManager,"shipping_dialog");
    }
}
