package com.belicoffee.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.belicoffee.Activity.LoginActivity;
import com.belicoffee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    RelativeLayout rlTopBar;
    BottomNavigationView bottomNavigationView;
    int itemAccount = -1;
    FirebaseUser mFirebaseUser;
    Dialog helpDialog;

    public MainFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MainFragment(int itemAccount) {
        this.itemAccount = itemAccount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rlTopBar = view.findViewById(R.id.rl_topbar_main);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_main);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        helpDialog = new Dialog(getContext());
        helpDialog.setContentView(R.layout.dialog_help);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_new);
        if (itemAccount != -1) {
            bottomNavigationView.setSelectedItemId(itemAccount);
        }

        helpDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        helpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        helpDialog.setCancelable(false);
        Button btnYes = helpDialog.findViewById(R.id.btn_yes_dialog);
        btnYes.setOnClickListener(this);
        Button btnNo = helpDialog.findViewById(R.id.btn_no_dialog);
        btnNo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.action_new:
                fragment = new NewsFragment();
                break;
            case R.id.action_shipping:
                fragment = new ShippingFragment();
                break;
            case R.id.action_promotion:
                fragment = new PromotionFragment();
                break;
            case R.id.action_shop:
                if (mFirebaseUser != null) {
                    fragment = new ShopFragment();
                    Log.i("tag", "mFirebaseUser " + mFirebaseUser.getUid());
                } else {
                    Log.i("tag", "mFirebaseUser null");
                    helpDialog.show();
                }
                break;
            case R.id.action_account:
                fragment = new AccountFragment();
                break;
        }
        if (fragment != null)
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container_main, fragment)
                    .commit();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes_dialog:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                helpDialog.dismiss();
                break;
            case R.id.btn_no_dialog:
                helpDialog.dismiss();
        }
    }
}
