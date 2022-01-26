package com.belicoffee.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.belicoffee.Fragment.AccountFragment;
import com.belicoffee.Fragment.BranchFragment;
import com.belicoffee.Fragment.CallFragment;
import com.belicoffee.Fragment.MainFragment;
import com.belicoffee.Fragment.MessageFragment;
import com.belicoffee.Fragment.ReceiveCallFragment;
import com.belicoffee.Fragment.SendCallFragment;
import com.belicoffee.Fragment.ShippingFragment;
import com.belicoffee.Fragment.UserFragment;
import com.belicoffee.Manager.IMainView;
import com.belicoffee.Manager.NewsManager;
import com.belicoffee.Manager.ScreenManager;
import com.belicoffee.Manager.ShippingManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.R;
import com.belicoffee.Service.MyInstanceIdService;
import com.belicoffee.Manager.CallManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements IMainView, CallManager.CallNotification {
    ShippingManager shippingManager;
    ScreenManager screenManager;
    int screenIdNotification = 0;
    String senderNotification = null;
    FirebaseUser mFirebaseUser;
    UserManager userManager;
    CallManager callManager;

    final int HOME_ID = 1, LIST_ID = 2, RECEIPT_ID = 3, FAVOURITE_ID = 4, HELP_ID = 5, CHAT_ID = 6, ACCOUNT_ID = 7, MAIN_ID = 8, SEND_CALL_ID = 9, RECEIVE_CALL_ID = 10, CALL_ID = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new MainFragment(), null).commit();

        screenManager = ScreenManager.getInstance();
        screenManager.setListener(this);

        shippingManager = ShippingManager.getInstance(this);
        shippingManager.getResponse();

        screenIdNotification = getIntent().getIntExtra("screenId", 0);
        senderNotification = getIntent().getStringExtra("sender");

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userManager = UserManager.getInstance();
        callManager = CallManager.getInstance();
        callManager.setListener(this);

        if (mFirebaseUser != null) {
            userManager.loadUserList(mFirebaseUser.getUid());
            MyInstanceIdService.sendRegistationToServer(FirebaseInstanceId.getInstance().getToken());
            callManager.joinSocket(mFirebaseUser.getUid());
            callManager.createPeerConnection(this);
            callManager.createMediaStream(this);
        }
        NewsManager newsManager=NewsManager.getInstance();
        newsManager.getNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (screenIdNotification != 0 && !senderNotification.equals(null)) {
            openScreen(screenIdNotification, senderNotification, null,-1);
        }
    }

    @Override
    protected void onDestroy() {
        callManager.leaveSocket();
        super.onDestroy();
    }

    @Override
    public void openScreen(int screenId) {
        switch (screenId) {
            case HOME_ID:
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case LIST_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_main, new ShippingFragment()).commit();
                break;
            case RECEIPT_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new BranchFragment()).commit();
                break;
            case FAVOURITE_ID:
                break;
            case HELP_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new UserFragment()).commit();
                break;
            case ACCOUNT_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_main, new AccountFragment()).commit();
            case MAIN_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new MainFragment(R.id.action_account)).commit();
                break;
        }
    }

    @Override
    public void openScreen(int screenId, String hisId, String hisName,int type) {
        switch (screenId) {
            case CHAT_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new MessageFragment(hisId)).commit();
                break;
            case CALL_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new CallFragment(hisId,type)).commit();
                break;
            case SEND_CALL_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new SendCallFragment(hisName, hisId)).commit();
                break;
            case RECEIVE_CALL_ID:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_full, new ReceiveCallFragment(hisName, hisId)).commit();
                break;
        }
    }

    @Override
    public void requestPermission() {
        int permission_internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permission_access_fine_location = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_call_phone = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int permission_camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permission_microphone = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);


        if (permission_internet != PackageManager.PERMISSION_GRANTED
                || permission_access_fine_location != PackageManager.PERMISSION_GRANTED
                || permission_call_phone != PackageManager.PERMISSION_GRANTED
                || permission_camera != PackageManager.PERMISSION_GRANTED
                || permission_microphone != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
    }

    @Override
    public void onNotifyCall(String hisName, String hisId) {
        openScreen(10, hisId, hisName,-1);
    }
}
