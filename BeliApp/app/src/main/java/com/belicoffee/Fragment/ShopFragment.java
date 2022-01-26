package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.belicoffee.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        spinner = view.findViewById(R.id.spn_branch_shop);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Bản đồ");
        arrayList.add("Cửa hàng S1");
        arrayList.add("Cửa hàng S2");
        arrayList.add("Cửa hàng S3");
        arrayList.add("Cửa hàng S5");
        arrayList.add("Cửa hàng S6");
        arrayList.add("Cửa hàng S7");
        arrayList.add("Cửa hàng S9");
        arrayList.add("Cửa hàng S10");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_shop, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_shop);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        return view;
    }

    @Override
    public void onResume() {
        spinner.setSelection(0);
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MapFragment();
                break;
            case 1:
                fragment = new BranchFragment();
                break;
            case 2:
                fragment = new BranchFragment();
                break;
            case 3:
                fragment = new BranchFragment();
                break;
            case 4:
                fragment = new BranchFragment();
                break;
            case 5:
                fragment = new BranchFragment();
                break;
            case 6:
                fragment = new BranchFragment();
                break;
            case 7:
                fragment = new BranchFragment();
                break;
            case 8:
                fragment = new BranchFragment();
                break;
        }
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fl_container_shop, fragment)
                .commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


