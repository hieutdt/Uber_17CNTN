package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Business.UserBusiness.UserUpdatedListener;
import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.Helpers.AppContext;
import com.example.cntn_grab.R;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private TextView mUserNameTxtView;
    private TextView mPhoneNumberTxtView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUserNameTxtView = view.findViewById(R.id.user_name_txt_view);
        mPhoneNumberTxtView = view.findViewById(R.id.phone_number_txt_view);
        final Button mLogoutBtn = view.findViewById(R.id.logout_button);
        final Button mRegisterDriverBtn = view.findViewById(R.id.register_driver_button);

        UserBusiness.getInstance().setUserUpdatedListener(new UserUpdatedListener() {
            @Override
            public void onUserUpdated(User user) {
                if (user == null) {
                    Log.d(TAG, "onUserUpdated: user is null");
                    return;
                }
                Log.d(TAG, "onUserUpdated: " + user.getName() + " " + user.getPhoneNumber());
                mUserNameTxtView.setText(user.getName());
                mPhoneNumberTxtView.setText(user.getPhoneNumber());
                if (user != null && user.getType() != "DRIVER") {
                    mRegisterDriverBtn.setVisibility(View.GONE);
                }
            }
        });

        User curUser = UserBusiness.getInstance().getCurrentUser();
        if (curUser != null) {
            mUserNameTxtView.setText(curUser.getName());
            mPhoneNumberTxtView.setText(curUser.getPhoneNumber());
        }

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserBusiness.getInstance().logOut();
                AppContext.getInstance().changeFragment(getActivity(), AppContext.getInstance().getLoginFragment());
            }
        });

        if (curUser != null && curUser.getType().equals("DRIVER")) {
            mRegisterDriverBtn.setVisibility(View.GONE);
        }
        mRegisterDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppContext.getInstance().changeFragment(getActivity(), AppContext.getInstance().getRegisterDriverFragment());
            }
        });

        return view;
    }

}
