package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntn_grab.R;


public class HomeFragment extends Fragment {
    private MapFragment map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        addMap();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void addMap() {
        this.map = new MapFragment();
        addFragment(map, false, "map");
    }

    private void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.map_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
