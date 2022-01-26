package com.belicoffee.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.belicoffee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class BranchFragment extends Fragment {
    FirebaseUser mFirebaseUser;
    String myId;
    Button btnOffer;
    Button btnAnswer;

    public BranchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_branch, container, false);
        btnOffer=view.findViewById(R.id.btn_offer_branch);
        btnAnswer=view.findViewById(R.id.btn_answer_branch);
        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        myId=mFirebaseUser.getUid();


        return view;
    }

}
