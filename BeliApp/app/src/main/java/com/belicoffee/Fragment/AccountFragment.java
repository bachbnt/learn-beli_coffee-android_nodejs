package com.belicoffee.Fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.belicoffee.Activity.LoginActivity;
import com.belicoffee.Adapter.OptionAdapter;
import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.Model.Option;
import com.belicoffee.R;
import com.belicoffee.Utility.SharedPreferencesUtility;
import com.belicoffee.Manager.CallManager;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    TextView tvUsername;
    ImageView ivAvatar;
    Button btnSignIn;
    LinearLayout llLogin;
    Button btnSignOut;
    ListView listView;
    boolean loginState;
    OptionAdapter optionAdapter;
    FirebaseUser mFirebaseUser;
    ScreenManager screenManager;
    String myEmail;
    Dialog helpDialog;
    UserManager userManager;
    CallManager callManager;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnSignIn = view.findViewById(R.id.btn_signin_account);
        btnSignOut = view.findViewById(R.id.btn_signout_account);
        llLogin = view.findViewById(R.id.ll_login_account);
        listView = view.findViewById(R.id.lv_option_account);
        tvUsername = view.findViewById(R.id.et_username_account);
        ivAvatar = view.findViewById(R.id.iv_avatar_account);

        helpDialog = new Dialog(getContext());
        helpDialog.setContentView(R.layout.dialog_help);
        Button btnYes = helpDialog.findViewById(R.id.btn_yes_dialog);
        btnYes.setOnClickListener(this);
        Button btnNo = helpDialog.findViewById(R.id.btn_no_dialog);
        btnNo.setOnClickListener(this);
        helpDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        helpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        helpDialog.setCancelable(false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        screenManager = ScreenManager.getInstance();
        callManager = CallManager.getInstance();
        userManager = UserManager.getInstance();
        ArrayList<Option> objects = getMockupData();
        optionAdapter = new OptionAdapter(view.getContext(), R.layout.item_option, objects);
        listView.setAdapter(optionAdapter);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myEmail = SharedPreferencesUtility.loadEmailPreferences(getContext(), "myEmail");
        refreshView();
    }

    public void refreshView() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loginState = SharedPreferencesUtility.loadLoginPreferences(getContext(), "login");
        btnSignOut.setVisibility(View.INVISIBLE);
        llLogin.setVisibility(View.INVISIBLE);
        if (loginState == true) {
            tvUsername.setText(myEmail);
            Glide.with(getContext()).load(userManager.getMyAvatar(mFirebaseUser.getUid())).centerCrop().into(ivAvatar);
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            llLogin.setVisibility(View.GONE);
        }
    }

    public ArrayList<Option> getMockupData() {
        String[] titles = {"Trang chủ", "Danh sách món", "Quản lý đơn", "Món yêu thích", "Trò chuyện"};
        int[] icons = {R.drawable.ic_home_dark_cyan_32dp, R.drawable.ic_view_list_dark_cyan_32dp, R.drawable.ic_receipt_dark_cyan_32dp, R.drawable.ic_favorite_dark_cyan_32dp, R.drawable.ic_help_dark_cyan_32dp};

        ArrayList<Option> options = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Option option = new Option(icons[i], titles[i]);
            options.add(option);
        }
        return options;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                screenManager.openScreen(1);
                break;
            case 1:
                screenManager.openScreen(2);
                break;
            case 2:
                screenManager.openScreen(3);
                break;
            case 3:
                screenManager.openScreen(4);
                break;
            case 4:
                if (mFirebaseUser != null) {
                    screenManager.openScreen(5);
                } else
                    helpDialog.show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin_account:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_signout_account:
                callManager.leaveSocket();
                FirebaseAuth.getInstance().signOut();
                UserManager.getInstance().clearUsers();
                SharedPreferencesUtility.savePreferences(getContext(), false, null, null);
                refreshView();
                break;
            case R.id.btn_yes_dialog:
                btnSignIn.callOnClick();
                helpDialog.dismiss();
                break;
            case R.id.btn_no_dialog:
                helpDialog.dismiss();
        }
    }
}
