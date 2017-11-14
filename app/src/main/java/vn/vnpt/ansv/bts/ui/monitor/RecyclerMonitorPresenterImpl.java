package vn.vnpt.ansv.bts.ui.monitor;

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
        void callback(EStatus eStatus, List<MinSensorFullObj> listSensorObj);
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
            callback.callback(EStatus.APIKEY_INVAILABLE, null);

        } else if (userId.trim().length() < 1) {
            callback.callback(EStatus.USERID_INVAILABLE, null);

        } else {
            String url = Utils.BASE_URL + "monitor/sensor/" + userId + "/" + stationId;
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
                                for (int i = 0; i < listStation.size(); i++) {
                                    List<MinSensorFullObj> listSensorObj = listStation.get(i).getStationData().getSensorList().getList();
                                    callback.callback(EStatus.GET_SENSOR_OBJ_SUCCESS, listSensorObj);

//                                    itemCardView = new CardviewObject();
//                                    itemCardView.setId(id);
//                                    itemCardView.setTenTram(tenTram);
                                    /*for (int z = 0; z < listSensorObj.size(); z++) {
                                        int sensorId = listSensorObj.get(z).getSensorInfo().getSensorId();
                                        String sensorName = listSensorObj.get(z).getSensorInfo().getSensorName();
                                        String sensorSerial = listSensorObj.get(z).getSensorInfo().getSensorSerial();
                                        int sensorTypeId = listSensorObj.get(z).getSensorInfo().getSensorTypeId();
                                        String measurementUnit = listSensorObj.get(z).getSensorInfo().getMeasurementUnit();
                                        int warningModeId = listSensorObj.get(z).getSensorInfo().getWarningModeId();
                                        int warningValue1 = listSensorObj.get(z).getSensorInfo().getWarningValue1();
                                        int warningValue2 = listSensorObj.get(z).getSensorInfo().getWarningValue2();
                                        int warningComp = listSensorObj.get(z).getSensorInfo().getWarningComp();

                                        SensorInfoObj sensorInfoObj = new SensorInfoObj(sensorId,
                                                sensorName, sensorSerial, sensorTypeId,
                                                measurementUnit, warningModeId,
                                                warningValue1, warningValue2, warningComp);

                                        List<SensorDataObj> sensorData = listSensorObj.get(z).getSensorData().getList();
                                        String sensorValue = String.valueOf(sensorData.get(0).getValue());
                                    }*/
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.callback(EStatus.NETWORK_FAILURE, null);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("API_KEY", apiKey);
                    Log.i("0x00", apiKey);
                    return headers;
                }
            };
            queue.add(stringRequest);
        }
    }

//    @Override
//    public void startBackground(int intervalMS) {
//        view.startBackground(intervalMS);
//    }
//
//    @Override
//    public void stopBackground() {
//        view.stopBackground();
//    }
}
