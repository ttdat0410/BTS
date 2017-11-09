package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
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
    private Context context;
    private SharedPreferences sp;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    public SplashPresenterImpl(Context context) {
        ((BTSApplication) context).getAppComponent().inject(this);
        this.context = context;
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
            splashView.showLoading();
            String url = null;
            RequestQueue queue = Volley.newRequestQueue(context);
            try {
                url = Utils.BASE_URL+"apikey/login?username="+user.trim()+"&password="+Utils.SHA1(pass.trim());
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                splashView.hideLoading();
                                JSONObject obj = null;
                                try {
                                    obj = new JSONObject(response);
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
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.callback(EStatus.NETWORK_FAILURE);
                        splashView.hideLoading();
                    }
                });
                queue.add(stringRequest);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getStations() {

    }
}
