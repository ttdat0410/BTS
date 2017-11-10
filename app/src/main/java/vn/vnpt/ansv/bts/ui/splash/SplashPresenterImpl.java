package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.objects.CardviewObject;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.objects.MinStationFullListObj;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.objects.SensorDataObj;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
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
        void callback(EStatus eStatus, String apiKey, String userId);
    }
    public interface GetStationCallback {
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
            callback.callback(EStatus.USERNAME_IS_EMPTY, "", "");

        } else if (pass.trim().isEmpty()) {
            callback.callback(EStatus.PASSWORD_IS_EMPTY, "", "");

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
                                JSONObject obj = null;
                                try {
                                    obj = new JSONObject(response);
                                    int statusServerCode = obj.getJSONObject("status").getInt("statusCode");
                                    if (statusServerCode == StatusServer.Success.getValue()) {
                                        String apiKey = obj.getJSONObject("data").getString("apiKey");
                                        String userId = obj.getJSONObject("data").getString("userId");
                                        callback.callback(EStatus.LOGIN_SUCCESS, apiKey, userId);

                                    } else if (statusServerCode == StatusServer.UsernameOrPasswordIsIncorrect.getValue()) {
                                        callback.callback(EStatus.USERNAME_INCORRECT, "", "");
                                        splashView.hideLoading();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.callback(EStatus.NETWORK_FAILURE, "", "");
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
    public void getStations(final GetStationCallback callback) {
        BTSPreferences preferences = preferenceManager.getPreferences();
        String userId = preferences.userId;
        final String apiKey = preferences.apiKey;

        if (apiKey.trim().length() < 1) {
            callback.callback(EStatus.APIKEY_INVAILABLE);

        } else if (userId.trim().length() < 1) {
            callback.callback(EStatus.USERID_INVAILABLE);

        } else {
            String url = Utils.BASE_URL + "monitor/sensor/" + userId;
            RequestQueue queue = Volley.newRequestQueue(context);
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Integer id;
                            String tenTram;
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject tram = object.getJSONObject("data");
                                Gson gson = new GsonBuilder().create();
                                MinStationFullListObj minStationFullListObj = gson.fromJson(tram.toString(), MinStationFullListObj.class);
                                List<MinStationFullObj> listStation = minStationFullListObj.getList();
                                callback.callback(EStatus.GET_STATIONS_SUCCESS);
                                splashView.launchMonitor(listStation);
                                splashView.hideLoading();
                                /*CardviewObject itemCardView;
                                Log.i("0x00", "COUNT: " + listStation.size());
                                for (int i = 0; i < listStation.size(); i++) {

                                    id = listStation.get(i).getStationInfo().getStationId();
                                    tenTram = listStation.get(i).getStationInfo().getStationName();
                                    List<MinSensorFullObj> listSensorObj = listStation.get(i).getStationData().getSensorList().getList();
                                    itemCardView = new CardviewObject();
                                    itemCardView.setId(id);
                                    itemCardView.setTenTram(tenTram);
                                    for (int z = 0; z < listSensorObj.size(); z++) {
                                        Integer sensorId = listSensorObj.get(z).getSensorInfo().getSensorTypeId();
                                        List<SensorDataObj> sensorData = listSensorObj.get(z).getSensorData().getList();                                     String sensorValue = String.valueOf(sensorData.get(0).getValue());
                                        switch (sensorId) {
                                            case 6:
                                                itemCardView.setNhietdo(sensorValue);
                                                break;
                                            case 7:
                                                itemCardView.setDoam(sensorValue);
                                                break;
                                            case 20:
                                                itemCardView.setCua(sensorValue);
                                                break;
                                            case 21:
                                                itemCardView.setKhoi(sensorValue);
                                                break;
                                            case 22:
                                                itemCardView.setBaochay(sensorValue);
                                                break;
                                            case 23:
                                                itemCardView.setMucnuoc(sensorValue);
                                                break;
                                        }
                                    }
//                                listItem.add(itemCardView);
//                                adapter.notifyDataSetChanged();
                                }*/
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("API_KEY", apiKey);
                    return headers;
                }
            };
            queue.add(stringRequest);

        }
    }
}
