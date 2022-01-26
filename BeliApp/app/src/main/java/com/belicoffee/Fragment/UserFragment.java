package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.belicoffee.Adapter.UserAdapter;
import com.belicoffee.Dialog.UserDialog;
import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.Model.ChatUser;
import com.belicoffee.R;
import com.belicoffee.Utility.SharedPreferencesUtility;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements UserAdapter.OnItemClickListener, UserAdapter.OnItemLongClickListener, View.OnClickListener, UserManager.UserListener {
    RecyclerView recyclerViewVer;
    RecyclerView recyclerViewHor;
    UserAdapter userAdapterVer;
    UserAdapter userAdapterHor;
    ImageButton ibBack;
    String myId;
    String myName;
    ArrayList<ChatUser> chatUsers;
    ScreenManager screenManager;
    UserManager userManager;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ibBack = view.findViewById(R.id.ib_back_help);
        chatUsers = new ArrayList<>();
        myId = SharedPreferencesUtility.loadIdPreferences(getContext(), "myId");
        userManager = UserManager.getInstance();
        myName= userManager.getMyName(myId);
        userAdapterVer = new UserAdapter(getContext(), myId, myName, chatUsers, this, this, true);
        userAdapterHor = new UserAdapter(getContext(), myId, myName, chatUsers, this, this, false);
        recyclerViewVer = view.findViewById(R.id.rv_vertical_user_help);
        recyclerViewHor = view.findViewById(R.id.rv_horizontal_user_help);
        recyclerViewVer.setAdapter(userAdapterVer);
        recyclerViewVer.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHor.setAdapter(userAdapterHor);
        recyclerViewHor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        screenManager = ScreenManager.getInstance();
        ibBack.setOnClickListener(this);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        userManager.setUserListener(this);
        updateUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        userManager.setUserListener(null);
    }

    @Override
    public void onItemClick(ChatUser chatUser) {
        screenManager.openScreen(6, chatUser.getId(), null,-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_help:
                screenManager.openScreen(8);
                break;
        }
    }

    @Override
    public void updateUser() {
        userAdapterVer.updateData(userManager.getChatUsers());
        userAdapterVer.notifyDataSetChanged();
        userAdapterHor.updateData(userManager.getChatUsers());
        userAdapterHor.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(String myId, String myName, ChatUser chatUser) {
        showUserDialog(myId, myName, chatUser);
    }

    private void showUserDialog(String myId, String myName, ChatUser chatUser) {
        FragmentManager fragmentManager = getFragmentManager();
        UserDialog userDialog = new UserDialog(myId, myName, chatUser);
        userDialog.show(fragmentManager, "user_dialog");
    }
}
