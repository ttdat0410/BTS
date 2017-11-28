package vn.vnpt.ansv.bts.ui.monitor;

import android.content.Context;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.objects.MinStationFullListObj;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.utils.EStatus;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/10/2017.
 */
@ActivityScope
public class RecyclerMonitorPresenterImpl implements RecyclerMonitorPresenter {

    public interface MonitorCallback {
        void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj, String gatewaySerial);
    }

    private RecyclerMonitorView view;
    private Context context;
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    public RecyclerMonitorPresenterImpl(Context context) {
        (((BTSApplication)context).getAppComponent()).inject(this);
        this.context = context;
    }

    @Override
    public void setView(RecyclerMonitorView view) {
        this.view = view;
    }

    @Override
    public void getData(int stationId, final MonitorCallback callback) {
        BTSPreferences prefs = preferenceManager.getPreferences();
        String userId = prefs.userId;
        final String apiKey = prefs.apiKey;

        if (apiKey.trim().length() < 1) {
            callback.callback(EStatus.APIKEY_INVAILABLE, null, null);

        } else if (userId.trim().length() < 1) {
            callback.callback(EStatus.USERID_INVAILABLE, null, null);

        } else {
            String url = Utils.getBaseUrl(context) + "monitor/sensor/" + userId + "/" + stationId;
            RequestQueue queue = Volley.newRequestQueue(context);
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject data = object.getJSONObject("data");
                                Gson gson = new GsonBuilder().create();
                                MinStationFullListObj minStationFullListObj = gson.fromJson(data.toString(), MinStationFullListObj.class);
                                List<MinStationFullObj> listStation = minStationFullListObj.getList();
                                for (int i = 0; i < listStation.size(); i++) {
                                    String gatewaySerial = listStation.get(i).getStationInfo().getGatewaySerial();
                                    List<MinSensorFullObj> listSensorObj = listStation.get(i).getStationData().getSensorList().getList();

                                    /**
                                     * gán sensorName vào tempSensorName để sắp xếp hiển thị cảm biến theo tempSensorName
                                     * theo "Nhiet do ngoai troi" - "Do am ngoai troi" - ....
                                     * */
                                    for (int k = 0; k<listSensorObj.size(); k++) {

                                        String sensorSerial = listSensorObj.get(k).getSensorInfo().getSensorSerial();
                                        String sensorName = listSensorObj.get(k).getSensorInfo().getSensorName();

                                        if (sensorSerial.equalsIgnoreCase("SS00117D8D7E060")) {
                                            listSensorObj.get(k).getSensorInfo().setTempSensorName("AAAA0 " + sensorName);

                                        } else if (sensorSerial.equalsIgnoreCase("SS00117D8D7E070")) {
                                            listSensorObj.get(k).getSensorInfo().setTempSensorName("AAAA1 " + sensorName);

                                        } else {
                                            listSensorObj.get(k).getSensorInfo().setTempSensorName(sensorName);
                                        }
                                    }

                                    callback.callback(EStatus.GET_SENSOR_OBJ_SUCCESS, listSensorObj, gatewaySerial);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.callback(EStatus.NETWORK_FAILURE, null, null);
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
