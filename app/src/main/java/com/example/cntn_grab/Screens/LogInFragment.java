package com.example.cntn_grab.Screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.Business.DriverBusiness.DriverBusiness;
import com.example.cntn_grab.Business.PassengerBusiness.PassengerBusiness;
import com.example.cntn_grab.Business.UserBusiness.LogInWithEmailListener;
import com.example.cntn_grab.Business.UserBusiness.SignUpWithPhoneListener;
import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Data.Driver;
import com.example.cntn_grab.Data.Passenger;
import com.example.cntn_grab.Data.Type;
import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.Helpers.AppContext;
import com.example.cntn_grab.Helpers.LoadingHelper;
import com.example.cntn_grab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInFragment extends Fragment {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEmailEditText = view.findViewById(R.id.login_email);
        mPasswordEditText = view.findViewById(R.id.login_password);

        LinearLayout logInButton = view.findViewById(R.id.login_button_login);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TON HIEU", "Button login onClickListener");

                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                Log.i("TON HIEU", password);

                UserBusiness.getInstance().setLogInWithEmailListener(new LogInWithEmailListener() {
                    @Override
                    public void logInWithEmailDidStart() {
                        LoadingHelper.getInstance().showLoading(getActivity());
                    }

                    @Override
                    public void logInWithEmailDidEnd(Boolean isOk, FirebaseUser firebaseUser) {
                        LoadingHelper.getInstance().hideLoading(getActivity());

                        if (isOk == false) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("Đăng nhập thất bại");
                            builder.setNegativeButton("OK", null);
                            builder.show();
                            UserBusiness.getInstance().setUser(null);
                            return;
                        }

                        final String uid = firebaseUser.getUid();
                        // Fetch data by UID

                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("ValueEventListener", "onDataChange: " + dataSnapshot.child("users").child(uid).toString());
                                User user = dataSnapshot.child("users").child(uid).getValue(User.class);

                                if (user == null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Đăng nhập thất bại");
                                    builder.setNegativeButton("OK", null);
                                    builder.show();
                                    UserBusiness.getInstance().setUser(null);
                                    return;
                                }

                                Log.d("ValueEventListener", "onDataChange: " + user.getEmail());
                                Log.i("TonHieu", user.toString());

                                if (user.getType().equals("PASSENGER")) {
                                    Passenger passenger = new Passenger();
                                    passenger.setId(user.getId());
                                    passenger.setName(user.getName());
                                    passenger.setEmail(user.getEmail());
                                    passenger.setPhoneNumber(user.getPhoneNumber());
                                    passenger.setState("WAITING");

                                    PassengerBusiness.getInstance().setPassenger(passenger);
                                } else {
                                    Driver driver = new Driver();
                                    driver.setId(user.getId());
                                    driver.setName(user.getName());
                                    driver.setEmail(user.getEmail());
                                    driver.setPhoneNumber(user.getPhoneNumber());
                                    driver.setState("ONLINE");

                                    DriverBusiness.getInstance().setDriver(driver);
                                }

                                // Update current user in UserBusiness
                                UserBusiness.getInstance().setUser(user);

                                loadHomeFragment();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        mDatabaseRef.addValueEventListener(postListener);
                        AppContext.getInstance().changeFragment(getActivity(), AppContext.getInstance().getProfileFragment());
                    }
                });

                UserBusiness.getInstance().logInWithEmail(getActivity(), email, password);
            }
        });

        LinearLayout signUpButton = view.findViewById(R.id.login_button_register);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserBusiness.getInstance().hasLoggedInUser()) {
            AppContext.getInstance().changeFragment(getActivity(), AppContext.getInstance().getProfileFragment());
        }
    }

    private void loadHomeFragment() {
        AppContext.getInstance().setCurrentFragmentIndex(AppConst.HOME_FRAGMENT_INDEX);
        User currentUser = UserBusiness.getInstance().getCurrentUser();

        // Change fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (currentUser.getType().equals("DRIVER"))
            transaction.replace(R.id.frame_container, AppContext.getInstance().getDriverStartFragment());
        else
            transaction.replace(R.id.frame_container, AppContext.getInstance().getHomeFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

        // Set up navigation bar
        BottomNavigationView bottomMenu = getActivity().findViewById(R.id.bottom_navigation_bar);

        MenuItem homeItem = bottomMenu.getMenu().getItem(AppConst.HOME_FRAGMENT_INDEX);
        MenuItem profileItem = bottomMenu.getMenu().getItem(AppConst.PROFILE_FRAGMENT_INDEX);

        profileItem.setIcon(R.drawable.profile_not_active);
        homeItem.setIcon(R.drawable.home_active);
    }
}

