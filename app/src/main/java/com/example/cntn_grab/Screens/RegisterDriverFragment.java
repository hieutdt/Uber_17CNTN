package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Data.Type;
import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.Helpers.AppContext;
import com.example.cntn_grab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDriverFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register_driver, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        LinearLayout registerDriverButton = view.findViewById(R.id.register_driver_button);
        registerDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User curUser = UserBusiness.getInstance().getCurrentUser();
//                curUser.setType(Type.DRIVER);
                curUser.setType("DRIVER");
                UserBusiness.getInstance().setUser(curUser);
                AppContext.getInstance().backToPrevFragment(getActivity());
            }
        });

        LinearLayout cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppContext.getInstance().backToPrevFragment(getActivity());
            }
        });

        return view;
    }
}
