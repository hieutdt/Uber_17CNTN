package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Helpers.AppConst;
import com.example.cntn_grab.Helpers.LoadingHelper;
import com.example.cntn_grab.R;
import com.facebook.FacebookSdk;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.cntn_grab.Helpers.AppContext;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.fullyInitialize();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationView = findViewById(R.id.bottom_navigation_bar);

        // Cache all fragments to ArrayList
        mFragments = new ArrayList<>();
        mFragments.add(AppContext.getInstance().getHomeFragment());
        mFragments.add(AppContext.getInstance().getProfileFragment());

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        AppContext.getInstance().setCurrentFragmentIndex(AppConst.HOME_FRAGMENT_INDEX);
                        setUpNavigationItems(menuItem.getItemId());
                        loadFragment(AppContext.getInstance().getHomeFragment());
                        break;

                    case R.id.action_profile:
                        AppContext.getInstance().setCurrentFragmentIndex(AppConst.PROFILE_FRAGMENT_INDEX);
                        setUpNavigationItems(menuItem.getItemId());
                        loadFragment(AppContext.getInstance().getProfileFragment());
                        break;
                }

                return true;
            }
        });

        loadFragment(mFragments.get(AppContext.getInstance().getCurrentFragmentIndex()));
    }

    private void setUpNavigationItems(int id) {
        MenuItem homeItem = mNavigationView.getMenu().getItem(AppConst.HOME_FRAGMENT_INDEX);
        MenuItem profileItem = mNavigationView.getMenu().getItem(AppConst.PROFILE_FRAGMENT_INDEX);

        if (id == R.id.action_home) {
            profileItem.setIcon(R.drawable.profile_not_active);
            homeItem.setIcon(R.drawable.home_active);
        } else {
            profileItem.setIcon(R.drawable.profile_active);
            homeItem.setIcon(R.drawable.home_not_active);
        }
    }

    // This function is public to call by another fragments or activities
    public void loadFragment(Fragment fragment) {
        Fragment loadFragment = fragment;

        if (fragment == AppContext.getInstance().getProfileFragment() && UserBusiness.getInstance().hasLoggedInUser() == false) {
            // Open login fragment
            LogInFragment logInFragment = new LogInFragment();
            loadFragment = logInFragment;
        } else if (fragment == AppContext.getInstance().getProfileFragment()) {
            ProfileFragment profileFragment = new ProfileFragment();
            loadFragment = profileFragment;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, loadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
