package com.example.cntn_grab.Helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.R;
import com.example.cntn_grab.Screens.DriverStartFragment;
import com.example.cntn_grab.Screens.HomeFragment;
import com.example.cntn_grab.Screens.LogInFragment;
import com.example.cntn_grab.Screens.ProfileFragment;
import com.example.cntn_grab.Screens.RegisterDriverFragment;

public class AppContext {
    private static AppContext instance;

    private int currentFragmentIndex;
    private Fragment homeFragment;
    private Fragment loginFragment;
    private Fragment profileFragment;
    private Fragment driverStartFragment;
    private Fragment registerDriverFragment;

    private AppContext() {
        //TODO: init variables here
        currentFragmentIndex = 0;
        homeFragment = new HomeFragment();
        loginFragment = new LogInFragment();
        profileFragment = new ProfileFragment();
        driverStartFragment = new DriverStartFragment();
        registerDriverFragment = new RegisterDriverFragment();
    }

    public static AppContext getInstance() {
        if (instance == null)
            instance = new AppContext();

        return instance;
    }

    public int getCurrentFragmentIndex() {
        return currentFragmentIndex;
    }

    public void setCurrentFragmentIndex(int currentFragmentIndex) {
        this.currentFragmentIndex = currentFragmentIndex;
    }

    public Fragment getHomeFragment() {
        return homeFragment;
    }

    public void setHomeFragment(Fragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public Fragment getLoginFragment() {
        return loginFragment;
    }

    public void setLoginFragment(Fragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    public Fragment getProfileFragment() {
        return profileFragment;
    }

    public void setProfileFragment(Fragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public Fragment getDriverStartFragment() {
        return driverStartFragment;
    }

    public Fragment getRegisterDriverFragment() {
        return registerDriverFragment;
    }

    public void setRegisterDriverFragment(Fragment registerDriverFragment) {
        this.registerDriverFragment = registerDriverFragment;
    }

    public void changeFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void backToPrevFragment(FragmentActivity fragmentActivity) {
        fragmentActivity.getSupportFragmentManager().popBackStack();
    }
}
