package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.belicoffee.Manager.MapManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.R;
import com.belicoffee.Service.MyInstanceIdService;
import com.belicoffee.Utility.SharedPreferencesUtility;
import com.belicoffee.Manager.CallManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    ImageButton ibBack;
    EditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvForgot;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    UserManager userManager;
    CallManager callManager;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ibBack = view.findViewById(R.id.ib_back_signin);
        tvForgot = view.findViewById(R.id.tv_forgot_signin);
        tvSignUp = view.findViewById(R.id.tv_signup_signin);
        btnSignIn = view.findViewById(R.id.btn_signin_signin);
        etEmail = view.findViewById(R.id.et_email_signin);
        etPassword = view.findViewById(R.id.et_password_signin);
        etEmail.requestFocus();
        mFirebaseAuth = FirebaseAuth.getInstance();
        userManager = UserManager.getInstance();
        callManager = CallManager.getInstance();

        ibBack.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_signin:
                getActivity().finish();
                break;
            case R.id.tv_forgot_signin:
                getFragmentManager().beginTransaction().replace(R.id.fl_container_login, new ForgotFragment()).addToBackStack(null).commit();
                break;
            case R.id.tv_signup_signin:
                getFragmentManager().beginTransaction().replace(R.id.fl_container_login, new SignUpFragment()).addToBackStack(null).commit();
                break;
            case R.id.btn_signin_signin:
                String strEmail = etEmail.getText().toString();
                String strPassword = etPassword.getText().toString();

                if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ.", Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                SharedPreferencesUtility.savePreferences(getContext(), true, mFirebaseUser.getUid(), mFirebaseUser.getEmail());
                                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                userManager.loadUserList(mFirebaseUser.getUid());
                                callManager.joinSocket(mFirebaseUser.getUid());
                                callManager.createPeerConnection(getContext());
                                callManager.createMediaStream(getContext());
                                MyInstanceIdService.sendRegistationToServer(FirebaseInstanceId.getInstance().getToken());
                                getActivity().finish();
                                MapManager.getInstance();
                            } else {
                                Toast.makeText(getContext(), "Đăng nhập thất bại. Vui lòng kiểm trai lại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
