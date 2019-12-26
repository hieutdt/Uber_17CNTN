package com.example.cntn_grab.Helpers;

import androidx.fragment.app.Fragment;

import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.Screens.DriverStartFragment;
import com.example.cntn_grab.Screens.HomeFragment;
import com.example.cntn_grab.Screens.ProfileFragment;

public class AppContext {
    private static AppContext instance;

    private int currentFragmentIndex;
    private Fragment homeFragment;
    private Fragment profileFragment;
    private Fragment driverStartFragment;

    private AppContext() {
        //TODO: init variables here
        currentFragmentIndex = 0;
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        driverStartFragment = new DriverStartFragment();
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

    public Fragment getDriverStartFragment() {
        return driverStartFragment;
    }
}
