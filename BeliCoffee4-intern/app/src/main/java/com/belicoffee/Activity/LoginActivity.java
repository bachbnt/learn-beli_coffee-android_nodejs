package com.belicoffee.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.belicoffee.Fragment.SignInFragment;
import com.belicoffee.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_login,new SignInFragment()).addToBackStack(null).commit();
    }
}
