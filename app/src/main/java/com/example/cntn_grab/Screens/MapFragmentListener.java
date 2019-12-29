package com.example.cntn_grab.Screens;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */

public interface MapFragmentListener {
    void onMapReady();
    void drawRouteDidEnd(int distance);
}
