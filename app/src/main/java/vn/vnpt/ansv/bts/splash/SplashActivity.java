package vn.vnpt.ansv.bts.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import kotlin.Pair;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.monitor.MonitorContainer;
import vn.vnpt.ansv.bts.utils.BTSToast;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/7/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int TIMER = 2000;
    private int animationDuration;
    private final LinearInterpolator linearInterpolator = new LinearInterpolator();

    @Inject
    SplashPresenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.mm_logo)
    ImageView mmLogo;

    @Bind(R.id.bottom_panel)
    FrameLayout bottomPanel;

    @Bind(R.id.name_edit_text)
    EditText nameEditText;

    @Bind(R.id.password_edit_text)
    EditText passwordEditText;

    @Bind(R.id.loginButton)
    Button loginButton;

    private SharedPreferences SP;

    private String username = null;
    private String password = null;
    private String PREFS_NAME = "GSTBTS_login";

    public String APIkey,userID,status;

    private final List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {{
        add(new Pair<String, String>("foo1", "bar1"));
        add(new Pair<String, String>("foo2", "bar2"));
    }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((BTSApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        checkNetwork();
        initToolbar();
        animationDuration = 300;
        initializeItems();
        tryToShowSplashImageView(TIMER);
        setupSharePreference();
        loginButton.setOnClickListener(onCustomClick);

    }

    private View.OnClickListener onCustomClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.loginButton:
                    login();
                    break;
            }
        }
    };

    private void setupSharePreference() {
        SP = getSharedPreferences(PREFS_NAME, 0);
        username = SP.getString("username", "");
        password = SP.getString("password", "");
        nameEditText.setText(username);
        passwordEditText.setText(password);
    }

    private void checkNetwork() {
        if(!presenter.isNetwork(this)){
            Intent enableBtIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            this.startActivityForResult(enableBtIntent, 1);
        }
    }

    public boolean validate() {
        boolean valid = true;
        username = nameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (username.isEmpty()){
            valid = false;


        } else {

        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            valid = false;

        } else {

        }
        return valid;
    }

    public void onLoginFailed() {
        showToast("Đăng nhập thất bại.", SuperToast.Background.RED);
        loginButton.setEnabled(true);
    }
    private ProgressDialog dialog;
    private void loginWaiting(){
        dialog = new ProgressDialog(SplashActivity.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage("Đang xác thực...");
        dialog.setTitle("Đăng nhập");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public void rememberUser(boolean remember, String username, String password) {

        SharedPreferences.Editor editor = SP.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("remember", remember);
        editor.commit();
    }

    private void login(){
        if (!validate()) {

            onLoginFailed();
            return;
        }
        loginButton.setEnabled(true);
        String url = null;
        try {
            url = getString(R.string.url)+"apikey/login?username="+username+"&password="+ Utils.SHA1(password);
            Fuel.get(url, params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(@NotNull com.github.kittinunf.fuel.core.Request request, @NotNull com.github.kittinunf.fuel.core.Response response, String s) {
                    startActivity(new Intent(getApplicationContext(), MonitorContainer.class));
                }

                @Override
                public void failure(@NotNull com.github.kittinunf.fuel.core.Request request, @NotNull com.github.kittinunf.fuel.core.Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG + " ERR", response + " " + fuelError);

                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //##############################################################################################
    /**
     * initToolbar
     *
     * Sets up the toolbar, adds margin to top of toolbar; this is needed for devices running
     * Lollipop or greater. If the device is running Kitkat or below, getStatusBarHeight will
     * return 0.
     *
     */
    private void initToolbar() {
//        toolbar.setBackgroundColor(getResourceColor(R.color.transparent));
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
//        params.setMargins(0, getStatusBarHeight(), 0, 0);
//        toolbar.setLayoutParams(params);
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
                animateItems();
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
//        if (loadingProgress.getIndeterminateDrawable() != null) {
//            loadingProgress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//        } else {
//        }
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

    private void showToast(String content, int color) {
        new BTSToast(this).showToast(content, color);
    }

}
