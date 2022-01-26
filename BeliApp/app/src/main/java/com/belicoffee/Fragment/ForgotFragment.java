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
import android.widget.Toast;

import com.belicoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotFragment extends Fragment implements View.OnClickListener {
    ImageButton ibBack;
    EditText etEmail;
    Button btnGet;
    FirebaseAuth mAuth;

    public ForgotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        ibBack = view.findViewById(R.id.ib_back_forgot);
        etEmail = view.findViewById(R.id.et_email_forgot);
        btnGet = view.findViewById(R.id.btn_get_forgot);
        etEmail.requestFocus();
        ibBack.setOnClickListener(this);
        btnGet.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_forgot:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_get_forgot:
                String strEmail = etEmail.getText().toString();
                if (TextUtils.isEmpty(strEmail)) {
                    Toast.makeText(getContext(), "Vui lòng nhập Email.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Link tạo mới Mật khẩu đã được gửi đến Email của bạn.", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), "Lấy lại Mật khẩu thất bại. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}
