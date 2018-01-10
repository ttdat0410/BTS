package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;
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
import vn.vnpt.ansv.bts.objects.MinStationFullListObj;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
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

    /**
     * interface cho phép gọi callback trong quá trình đăng nhập
     * @see EStatus
     * */
    public interface Callback {
        /**
         * Hàm callback
         * @param eStatus trạng thái view
         * @param apiKey api key lấy được từ server
         * @param userId user id lấy được từ server
         * */
        void callback(EStatus eStatus, String apiKey, String userId);
    }

    /**
     * interface cho phép gọi callback trong quá trình lấy danh sách các trạm
     * @see EStatus
     * */
    public interface GetStationCallback {
        /**
         * Hàm callback
         * @param eStatus trạng thái view
         * */
        void callback(EStatus eStatus);
    }

    private SplashView splashView;
    private Context context;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    public SplashPresenterImpl(Context context) {
        // inject tới dagger
        ((BTSApplication) context).getAppComponent().inject(this);
        this.context = context;
    }

    /**
     * Hàm implement setView
     * @see SplashPresenter
     * @param splashView view từ model MVP
     * */
    @Override
    public void setView(SplashView splashView) {
        this.splashView = splashView;
        splashView.showBottomView();
    }

    /**
     * Hàm implement checkNetwork
     * @see SplashPresenter
     * @param context đối tượng có quyền sử dụng
     * @return boolean
     * */
    @Override
    public boolean checkNetwork(Context context) {
        return Utils.isNetworkAvailable(context);
    }

    /**
     * Hàm implement getUser cho phép gửi request để đăng nhập
     * @see SplashPresenter
     * @param user tên đăng nhập
     * @param pass mật khẩu
     * @param callback
     * */
    @Override
    public void getUser(String user, String pass, final Callback callback) {
        /**
         * Kiểm tra nếu user hoặc mật khẩu rỗng thì trả về trạng thái USERNAME_IS_EMPTY hoặc PASSWORD_IS_EMPTY
         * @see  EStatus
         * */
        if (user.trim().isEmpty()) {
            callback.callback(EStatus.USERNAME_IS_EMPTY, "", "");

        } else if (pass.trim().isEmpty()) {
            callback.callback(EStatus.PASSWORD_IS_EMPTY, "", "");

        } else {
            /**
             * Cho phép view gọi showLoading khi user và pass đều có nội dung
             * @see SplashView
             * */
            splashView.showLoading();
            String url = null;
            RequestQueue queue = Volley.newRequestQueue(context);
            try {
                url = Utils.getBaseUrl(context)+"apikey/login?username="+user.trim()+"&password="+Utils.SHA1(pass.trim());
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
                                        /**
                                         * Khi lấy được api và userId thì trả về callback SUCCESS,
                                         * ngược lại trả về trạng thái server
                                         * */
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
                    /**
                     * Trường hợp mất kết nối server
                     * */
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

    /**
     * Hàm implement getStation cho phép request để lấy danh sách gateway
     * @see SplashPresenter
     * @param callback
     * */
    @Override
    public void getStations(final GetStationCallback callback) {
        BTSPreferences preferences = preferenceManager.getPreferences();
        String userId = preferences.userId;
        final String apiKey = preferences.apiKey;

        /**
         * Kiểm tra chắc chắn tồn tại apiKey và userId
         * */
        if (apiKey.trim().length() < 1) {
            callback.callback(EStatus.APIKEY_INVAILABLE);

        } else if (userId.trim().length() < 1) {
            callback.callback(EStatus.USERID_INVAILABLE);

        } else {
            String url = Utils.getBaseUrl(context) + "monitor/sensor/" + userId;
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

    @Override
    public void getRoleId() {
        BTSPreferences prefs = preferenceManager.getPreferences();
        String userId = prefs.userId;
        final String apiKey = prefs.apiKey;
        String url = Utils.getBaseUrl(context) + "account/getMyAccountInfo/" + userId;
        RequestQueue queue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject data = object.getJSONObject("data");
                            Log.e("0x00", data.toString() );
                            BTSPreferences prefs = preferenceManager.getPreferences();
                            if (data.has("roleId") && data.has("role")) {
                                Log.i("0x00", data.getString("roleId"));
                                prefs.roleId = data.getString("roleId");
                                preferenceManager.setPreferences(prefs);
                            } else {
                                prefs.roleId = data.getString("");
                                preferenceManager.setPreferences(prefs);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
