package vn.vnpt.ansv.bts.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.ui.BTSSplashActivity;
import vn.vnpt.ansv.bts.monitor.MonitorContainer;

/**
 * Created by ANSV on 11/7/2017.
 */

public class SplashActivity extends BTSSplashActivity {

    private static final int TIMER = 2000;
    private int animationDuration;
    private final LinearInterpolator linearInterpolator = new LinearInterpolator();


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.mm_logo)
    ImageView mmLogo;

    @Bind(R.id.bottom_panel)
    FrameLayout bottomPanel;

    @Bind(R.id.scanner_device_list)
    RecyclerView scannerRecyclerView;

    @Bind(R.id.bluetooth_devices_view)
    View bluetoothDevicesView;

    @Bind(R.id.no_network)
    View noNetwork;

    @Bind(R.id.net_status_report)
    TextView netStatusReport;

    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        component().inject(this);
        ButterKnife.bind(this);
        initToolbar();
        animationDuration = 300;
        initializeItems();
        tryToShowSplashImageView(TIMER);
    }

    /**
     * initToolbar
     *
     * Sets up the toolbar, adds margin to top of toolbar; this is needed for devices running
     * Lollipop or greater. If the device is running Kitkat or below, getStatusBarHeight will
     * return 0.
     *
     */
    private void initToolbar() {
        toolbar.setBackgroundColor(getResourceColor(R.color.transparent));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
    }

    /**
     * tryToShowSplashImageView
     *
     * Hàm hiên thị splash khi mới mở app
     * @param timer số giây để bắt đầu ẩn splash và bắt đầu quét mã
     * */
    private void tryToShowSplashImageView(int timer) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(), MonitorContainer.class));
                animateItems();
                bluetoothDevicesView.setVisibility(View.GONE);
                noNetwork.setVisibility(View.VISIBLE);
                netStatusReport.setText("S...");
                configureProgressIndicator();
            }
        }, timer);
    }

    private void initializeItems() {
        bottomPanel.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        mmLogo.setVisibility(View.VISIBLE);
    }

    /**
     * configureProgressIndicator
     *
     * Sets the color of the indeterminate progress indicator to be blue,
     * as defined in res/values/color.xml
     *
     */
    private void configureProgressIndicator() {
        int color;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            color = getResources().getColor(R.color.sl_terbium_green);
        } else {
            color = getResources().getColor(R.color.sl_terbium_green, null);
        }
        if (loadingProgress.getIndeterminateDrawable() != null) {
            loadingProgress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else {
        }
    }

    private void animateItems() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> animatorList = new ArrayList<>();

        animatorList.add(mmLogo.getVisibility() == View.VISIBLE ? animateLogo() : null);
        animatorList.add(toolbar.getVisibility() == View.INVISIBLE ? animateToolbar() : null);
        animatorList.add(bottomPanel.getVisibility() == View.INVISIBLE ? animateBottomPanel() : null);

        animatorSet.playTogether(animatorList);
        animatorSet.start();
    }

    private Animator animateLogo() {
        ObjectAnimator mmLogoAnimator = ObjectAnimator.ofFloat(mmLogo, "alpha", 1.0f, 0.0f);
        mmLogoAnimator.setDuration(animationDuration);
        mmLogoAnimator.setInterpolator(linearInterpolator);
        mmLogoAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mmLogo.setVisibility(View.INVISIBLE);
            }
        });
        return mmLogoAnimator;
    }

    private Animator animateToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        ObjectAnimator toolbarAnimator = ObjectAnimator.ofFloat(toolbar, "alpha", 0.0f, 1.0f);
        toolbarAnimator.setDuration(animationDuration);
        toolbarAnimator.setInterpolator(linearInterpolator);
        return toolbarAnimator;
    }

    private Animator animateBottomPanel() {
        bottomPanel.setVisibility(View.VISIBLE);
        ObjectAnimator bottomPanelAnimator = ObjectAnimator.ofFloat(bottomPanel, "translationY", (float) bottomPanel.getHeight(), 0.0f);
        bottomPanelAnimator.setDuration(animationDuration);
        bottomPanelAnimator.setInterpolator(linearInterpolator);
        return bottomPanelAnimator;
    }

}
