package vn.vnpt.ansv.bts.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.johnpersano.supertoasts.SuperToast;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.utils.BTSToast;
import vn.vnpt.ansv.bts.utils.EStatus;

/**
 * Created by ANSV on 11/9/2017.
 */

public class SplashActivity extends AppCompatActivity implements SplashView{

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int TIMER = 2000;
    private int animationDuration;
    private final LinearInterpolator linearInterpolator = new LinearInterpolator();

    @Inject
    PreferenceManager prefsManager;

    @Inject
    SplashPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mm_logo)
    ImageView mmLogo;
    @BindView(R.id.bottom_panel)
    FrameLayout bottomPanel;
    @BindView(R.id.name_edit_text)
    EditText nameEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;

    private SharedPreferences SP;
    private String username = null;
    private String password = null;
    private String PREFS_NAME = "GSTBTS_login";

    public String APIkey,userID,status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((BTSApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        if (!presenter.checkNetwork(this)) {
            Intent enableBtIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            this.startActivityForResult(enableBtIntent, 1);
        }
        initToolbar();
        animationDuration = 300;
        initializeItems();
        setupSharePreference();
        loginButton.setOnClickListener(onSplashClick);
    }

    private View.OnClickListener onSplashClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginButton:
                    loginButton.setEnabled(false);
                    presenter.getUser(nameEditText.getText().toString().trim(),
                            passwordEditText.getText().toString().trim(), new SplashPresenterImpl.Callback() {
                                @Override
                                public void callback(EStatus eStatus) {
                                    updateStatusView(eStatus);
                                    loginButton.setEnabled(true);
                                }
                            });
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

    public void saveAccount(String username, String password) {

        SharedPreferences.Editor editor = SP.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    private void updateStatusView(EStatus eStatus) {
        switch (eStatus) {
            case NETWORK_FAILURE:
                showToast(getResources().getString(R.string.verify_network_lost), SuperToast.Background.RED);
                break;
            case USERNAME_IS_EMPTY:
                showToast(getResources().getString(R.string.error_username_is_empty), SuperToast.Background.RED);
                nameEditText.requestFocus();
                break;
            case PASSWORD_IS_EMPTY:
                showToast(getResources().getString(R.string.error_password_is_empty), SuperToast.Background.RED);
                passwordEditText.requestFocus();
                break;
            case USERNAME_INCORRECT:
                showToast(getResources().getString(R.string.error_username_incorrect), SuperToast.Background.RED);
                nameEditText.requestFocus();
                break;
            case PASSWORD_INCORRECT:
                showToast(getResources().getString(R.string.error_password_incorrect), SuperToast.Background.RED);
                passwordEditText.requestFocus();
                break;
            case UESRNAME_UNMATCHED_WITH_FORMAT:
                showToast(getResources().getString(R.string.error_username_unmatched_with_format), SuperToast.Background.RED);
                nameEditText.requestFocus();
                break;
            case PASSWORD_UNMATCHED_WITH_FORMAT:
                showToast(getResources().getString(R.string.error_password_unmatched_with_format), SuperToast.Background.RED);
                passwordEditText.requestFocus();
                break;
            case CHECKING_ACCOUNT:
                showToast(getResources().getString(R.string.verify_checking_account), SuperToast.Background.WHITE);
                hideKeyboard(passwordEditText);
            case LOGIN:
                break;
            case LOGIN_SUCCESS:
                showToast(getResources().getString(R.string.verify_login_success), SuperToast.Background.GREEN);
                saveAccount(nameEditText.getText().toString(), passwordEditText.getText().toString());
                hideKeyboard(passwordEditText);
                presenter.getStations();
                break;
            case LOGIN_FAILURE:
                showToast(getResources().getString(R.string.verify_login_failure), SuperToast.Background.RED);
                break;
            default:
                break;
        }
    }

    private void hideKeyboard(EditText fromView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fromView.getWindowToken(), 0);
    }

    private ProgressDialog dialog;
    @Override
    public void showLoading() {
        dialog = new ProgressDialog(SplashActivity.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage(getResources().getString(R.string.verify_login));
        dialog.setTitle("Đăng nhập");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog.isShowing()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                }
            }, 500);
        }
    }

    @Override
    public void showBottomView() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateItems();
            }
        }, TIMER);
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

    private void initializeItems() {
        bottomPanel.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        mmLogo.setVisibility(View.VISIBLE);
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
