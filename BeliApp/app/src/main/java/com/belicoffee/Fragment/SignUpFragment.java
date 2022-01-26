package com.belicoffee.Fragment;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.belicoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    ImageView ivAvatar;
    ImageButton ibBack;
    Button btnSignUp;
    EditText etUsername, etPassword, etEmail, etPhone;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;
    Uri avtUri;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ibBack = view.findViewById(R.id.ib_back_signup);
        ivAvatar = view.findViewById(R.id.iv_avatar_signup);
        btnSignUp = view.findViewById(R.id.btn_signup_signup);
        etUsername = view.findViewById(R.id.et_username_signup);
        etPassword = view.findViewById(R.id.et_password_signup);
        etEmail = view.findViewById(R.id.et_email_signup);
        etPhone = view.findViewById(R.id.et_phone_signup);
        etUsername.requestFocus();

        mAuth = FirebaseAuth.getInstance();
        ibBack.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            avtUri = data.getData();
            ivAvatar.setImageURI(avtUri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_signup:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_signup_signup:
                String strUsername = etUsername.getText().toString();
                String strPassword = etPassword.getText().toString();
                String strEmail = etEmail.getText().toString();
                String strPhone = etPhone.getText().toString();

                if (avtUri == null) {
                    Toast.makeText(getContext(), "Vui lòng chọn 1 bức ảnh", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strPassword) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPhone)) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (strPassword.length() < 6) {
                    Toast.makeText(getContext(), "Mật khẩu phải chứa ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                } else if (strPhone.length() < 10 || strPhone.length() > 11) {
                    Toast.makeText(getContext(), "Số điện thoại phải là 10-11 chữ số", Toast.LENGTH_SHORT).show();
                } else {
                    registerAccount(strUsername, strPassword, strEmail, strPhone, avtUri);
                }
                break;
            case R.id.iv_avatar_signup:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void registerAccount(final String username, String password, final String email, final String phone, final Uri avtUri) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String id = firebaseUser.getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(id);
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("Avatars/" + id);
                    UploadTask uploadTask = mStorageRef.putFile(avtUri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", id);
                    hashMap.put("username", username);
                    hashMap.put("email", email);
                    hashMap.put("phone", phone);
                    mDatabaseRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                getFragmentManager().popBackStack();
                            }
                        }
                    });
                    Toast.makeText(getContext(), "Đăng ký tài khoản thành công. Bây giờ bạn đã có thể sử dụng.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Đăng ký tài khoản thất bại. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
