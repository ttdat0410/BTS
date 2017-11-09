package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Pair;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.utils.EStatus;
import vn.vnpt.ansv.bts.utils.StatusServer;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

@ActivityScope
public class SplashPresenterImpl implements SplashPresenter {

    public interface Callback {
        void callback(EStatus eStatus);
    }
    private SplashView splashView;

    @Inject
    public SplashPresenterImpl(Context context) {
        ((BTSApplication) context).getAppComponent().inject(this);
    }

    @Override
    public boolean checkNetwork(Context context) {
        return Utils.isNetworkAvailable(context);
    }

    @Override
    public void setView(SplashView splashView) {
        this.splashView = splashView;
        splashView.showBottomView();
    }

    @Override
    public void getUser(String user, String pass, final Callback callback) {
        if (user.trim().isEmpty()) {
            callback.callback(EStatus.USERNAME_IS_EMPTY);
        } else if (pass.trim().isEmpty()) {
            callback.callback(EStatus.PASSWORD_IS_EMPTY);
        } else {
            callback.callback(EStatus.CHECKING_ACCOUNT);
            splashView.showLoading();
            String url = null;
            try {
                final List<Pair<String, String>> params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("foo1", "bar1"));
                    add(new Pair<String, String>("foo2", "bar2"));
                }};

                url = Utils.BASE_URL+"apikey/login?username="+user+"&password="+ Utils.SHA1(pass);
                Fuel.get(url, params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                    @Override
                    public void success(@NotNull com.github.kittinunf.fuel.core.Request request, @NotNull com.github.kittinunf.fuel.core.Response response, String str) {
                        if (response.getStatusCode() == 200) {
                            callback.callback(EStatus.LOGIN );
                            splashView.hideLoading();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(str);
                                int statusServerCode = obj.getJSONObject("status").getInt("statusCode");
                                if (statusServerCode == StatusServer.Success.getValue()) {
                                    callback.callback(EStatus.LOGIN_SUCCESS);
                                } else if (statusServerCode == StatusServer.UsernameOrPasswordIsIncorrect.getValue()) {
                                    callback.callback(EStatus.USERNAME_INCORRECT);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void failure(@NotNull com.github.kittinunf.fuel.core.Request request, @NotNull com.github.kittinunf.fuel.core.Response response, @NotNull FuelError fuelError) {
                        if (response.getStatusCode() == -1) {
                            callback.callback(EStatus.NETWORK_FAILURE);
                            splashView.hideLoading();
                        }
                    }
                });
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
