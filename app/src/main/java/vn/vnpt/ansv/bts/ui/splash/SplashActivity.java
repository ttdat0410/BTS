package vn.vnpt.ansv.bts.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.johnpersano.supertoasts.SuperToast;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorContainer;
import vn.vnpt.ansv.bts.ui.settings.SettingsActivity;
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

    @BindView(R.id.titleLogin)
    TextView titleLogin;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mm_vnpt_logo)
    ImageView mm_vnpt_logo;
    @BindView(R.id.mm_hospital_logo)
    ImageView mm_hospital_logo;
    @BindView(R.id.divLine)
    View divLine;
    @BindView(R.id.titleAppName)
    TextView titleAppName;
    @BindView(R.id.bottom_panel)
    FrameLayout bottomPanel;
    @BindView(R.id.name_edit_text)
    EditText nameEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.titleVNPT)
    TextView titleVNPT;

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

        animationDuration = 300;
        initToolbar();
        initializeItems();
        setupSharePreference();
        loginButton.setOnClickListener(onSplashClick);
        titleVNPT.setOnClickListener(onSplashClick);
    }

    private int countOfClicked = 0;
    private View.OnClickListener onSplashClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginButton:
                    loginButton.setEnabled(false);
                    presenter.getUser(nameEditText.getText().toString().trim(),
                            passwordEditText.getText().toString().trim(), new SplashPresenterImpl.Callback() {
                                @Override
                                public void callback(EStatus eStatus, String apiKey, String userId) {
                                    updateStatusView(eStatus, apiKey, userId);
                                    loginButton.setEnabled(true);
                                }
                            });
                    break;
                case R.id.titleVNPT:
                    /**
                     * sau 7 lần clicked, sẻ chuyển qua trang SettingsActivity class
                     * @see SettingsActivity
                     * */
                    countOfClicked++;
                    if (countOfClicked >= 7) {
                        countOfClicked = 0;
                        startActivity(new Intent(SplashActivity.this, SettingsActivity.class));
                    }
                    break;
            }
        }
    };

    /**
     * Hàm load nội dung trong Preference ra view
     * */
    private void setupSharePreference() {
        BTSPreferences prefs = prefsManager.getPreferences();
        nameEditText.setText(prefs.userName);
        passwordEditText.setText(prefs.password);
        /**
         * Gán trường ip và port trong Preference giá trị mặc định
         * */
        if (prefs.ip == null && prefs.port == null) {
            prefs.ip = "113.161.61.89";
            prefs.port = "40081";
            prefsManager.setPreferences(prefs);
        }
    }

    /**
     * Hàm lưu thông tin vào Preference
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @param apiKey apiKey
     * @param userId userId
     * */
    public void saveAccount(String username, String password, String apiKey, String userId) {
        BTSPreferences prefs = prefsManager.getPreferences();
        prefs.userName = username;
        prefs.password = password;
        prefs.apiKey = apiKey;
        prefs.userId = userId;
        prefsManager.setPreferences(prefs);
    }

    /**
     * Hàm cập nhật trạng thái view được trả về từ các callback
     * @see vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl.Callback
     * @see vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl.GetStationCallback
     * @param eStatus trạng thái view sẽ tồn tại
     * @param apiKey apikey
     * @param userId userId
     * */
    private void updateStatusView(EStatus eStatus, String apiKey, String userId) {
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
                saveAccount(nameEditText.getText().toString(), passwordEditText.getText().toString(), apiKey, userId);
                hideKeyboard(passwordEditText);
                tryToGetStations();
                break;
            case LOGIN_FAILURE:
                showToast(getResources().getString(R.string.verify_login_failure), SuperToast.Background.RED);
                break;
            case APIKEY_INVAILABLE:
                showToast(getResources().getString(R.string.error_api_key_invailable), SuperToast.Background.RED);
                break;
            case USERID_INVAILABLE:
                showToast(getResources().getString(R.string.error_user_id_invailable), SuperToast.Background.RED);
                break;
            case GET_STATIONS_SUCCESS:
//                showToast(getResources().getString(R.string.verify_get_stations_success), SuperToast.Background.GREEN);
                break;
            case GET_STATIONS_FAILURE:
                showToast(getResources().getString(R.string.verify_get_stations_failure), SuperToast.Background.RED);
                break;
            default:
                break;
        }
    }

    /**
     * Hàm ẩn keyboard
     * @param fromView từ view
     * */
    private void hideKeyboard(EditText fromView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fromView.getWindowToken(), 0);
    }

    /**
     * Hàm lấy danh sách các trạm theo presenter
     * @see SplashPresenter
     * */
    private void tryToGetStations() {
        presenter.getStations(new SplashPresenterImpl.GetStationCallback() {
            @Override
            public void callback(EStatus eStatus) {
                updateStatusView(eStatus, "", "");
                if (dialog.isShowing()) {
                    hideLoading();
                }
            }
        });
    }

    private ProgressDialog dialog;
    /**
     * Hàm implement từ view
     * @see SplashView
     * */
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

    /**
     * Hàm implement từ view
     * @see SplashView
     * */
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

    /**
     * Hàm implement từ view
     * @see SplashView
     * */
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

    /**
     * Hàm implement từ view
     * @see SplashView
     * */
    @Override
    public void launchMonitor(final List<MinStationFullObj> listStation) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MonitorContainer.launch(SplashActivity.this, listStation);
            }
        }, 500);
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

        if (Build.VERSION.SDK_INT < 23) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            toolbar.setBackgroundColor(getColor(R.color.transparent));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(params);
    }

    private void initializeItems() {
        bottomPanel.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        mm_vnpt_logo.setVisibility(View.VISIBLE);
        mm_hospital_logo.setVisibility(View.INVISIBLE);
        titleAppName.setVisibility(View.INVISIBLE);
        divLine.setVisibility(View.INVISIBLE);
    }

    private void animateItems() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> animatorList = new ArrayList<>();

        animatorList.add(mm_vnpt_logo.getVisibility() == View.VISIBLE ? animateLogo() : null);
        animatorList.add(toolbar.getVisibility() == View.INVISIBLE ? animateToolbar() : null);
        animatorList.add(bottomPanel.getVisibility() == View.INVISIBLE ? animateBottomPanel() : null);
        animatorList.add(mm_hospital_logo.getVisibility() == View.INVISIBLE ? animateHospitalLogo() : null);
        animatorList.add(titleAppName.getVisibility() == View.INVISIBLE ? animateAppName() : null);
        animatorList.add(divLine.getVisibility() == View.INVISIBLE ? animateDivLine() : null);

        animatorSet.playTogether(animatorList);
        animatorSet.start();
    }

    private Animator animateLogo() {
        ObjectAnimator mmLogoAnimator = ObjectAnimator.ofFloat(mm_vnpt_logo, "alpha", 1.0f, 0.0f);
        mmLogoAnimator.setDuration(animationDuration);
        mmLogoAnimator.setInterpolator(linearInterpolator);
        mmLogoAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mm_vnpt_logo.setVisibility(View.INVISIBLE);
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

    private Animator animateHospitalLogo() {
        mm_hospital_logo.setVisibility(View.VISIBLE);
        ObjectAnimator hospitalLogoAnimator = ObjectAnimator.ofFloat(toolbar, "alpha", 0.0f, 1.0f);
        hospitalLogoAnimator.setDuration(animationDuration);
        hospitalLogoAnimator.setInterpolator(linearInterpolator);
        return hospitalLogoAnimator;
    }

    private Animator animateAppName() {
        titleAppName.setVisibility(View.VISIBLE);
        ObjectAnimator titleAppNameAnimator = ObjectAnimator.ofFloat(toolbar, "alpha", 0.0f, 1.0f);
        titleAppNameAnimator.setDuration(animationDuration);
        titleAppNameAnimator.setInterpolator(linearInterpolator);
        return titleAppNameAnimator;
    }

    private Animator animateDivLine() {
        divLine.setVisibility(View.VISIBLE);
        ObjectAnimator divLineAnimator = ObjectAnimator.ofFloat(divLine, "alpha", 0.0f, 1.0f);
        divLineAnimator.setDuration(animationDuration);
        divLineAnimator.setInterpolator(linearInterpolator);
        return divLineAnimator;
    }

    private void showToast(String content, int color) {
        new BTSToast(this).showToast(content, color);
    }
}
