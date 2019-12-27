package com.example.cntn_grab.Helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.R;
import com.example.cntn_grab.Screens.HomeFragment;
import com.example.cntn_grab.Screens.ProfileFragment;

public class AppContext {
    private static AppContext instance;

    private int currentFragmentIndex;
    private Fragment homeFragment;
    private Fragment profileFragment;

    private AppContext() {
        //TODO: init variables here
        currentFragmentIndex = 0;
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
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

    public Fragment getProfileFragment() {
        return profileFragment;
    }

    public void setProfileFragment(Fragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public void changeFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
