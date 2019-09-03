package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belicoffee.Adapter.PagerAdapter;
import com.belicoffee.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ArrayList<Fragment> fragments;
    DrinkFragment drinkFragment;
    FoodFragment foodFragment;


    public ShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        fragments = new ArrayList<>();
        drinkFragment = new DrinkFragment();
        foodFragment = new FoodFragment();
        fragments.add(drinkFragment);
//        fragments.add(foodFragment);
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        tabLayout = view.findViewById(R.id.tablayout_shipping);
        viewPager = view.findViewById(R.id.viewpager_shipping);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
