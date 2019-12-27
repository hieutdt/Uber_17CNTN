package com.example.cntn_grab.Helpers;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.cntn_grab.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class LoadingHelper {
    private static LoadingHelper instance;
    private ProgressBar progressBar;
    private RelativeLayout layout;

    private LoadingHelper() {
    }

    public static LoadingHelper getInstance() {
        if (instance == null)
            instance = new LoadingHelper();
        return instance;
    }

    public void showLoading(Activity activity) {
        layout = new RelativeLayout(activity);
        layout.setBackgroundColor(activity.getColor(R.color.black));
        layout.setAlpha((float)0.3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        progressBar = new ProgressBar(activity);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(activity.getColor(R.color.green));
        progressBar.setIndeterminateDrawable(doubleBounce);

        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(200, 200);
        barParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        layout.addView(progressBar,  barParams);

        activity.addContentView(layout, params);

        // Disable user interaction
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideLoading(Activity activity) {
        layout.removeAllViews();
        layout.setAlpha(0);

        // Enable user interaction
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
