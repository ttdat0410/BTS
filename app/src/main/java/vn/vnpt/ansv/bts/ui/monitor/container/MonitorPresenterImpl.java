package vn.vnpt.ansv.bts.ui.monitor.container;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorFragment;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl;
import vn.vnpt.ansv.bts.utils.EStatus;
import vn.vnpt.ansv.bts.utils.NotificationUtils;
import vn.vnpt.ansv.bts.utils.Utils;
import vn.vnpt.technology.mqtt.VNPTClient;
import vn.vnpt.technology.mqtt.VNPTClientEventHandle;

/**
 * Created by ANSV on 11/11/2017.
 */

public class MonitorPresenterImpl implements MonitorPresenter {

    public interface MQTTCallback {
        void detectNoise(String message);
    }
    private enum Role {
        Admin("505"),
        Other("515");
        private final String value;
        Role(String value) {
            this.value = value;
        }
        String getValue() {
            return value;
        }
    }

    public static boolean isShowAlert = true;
    private MonitorView view;
    private Context context;

    @Inject
    PreferenceManager preferenceManager;

    public MonitorPresenterImpl(Context context) {
        (((BTSApplication) context).getAppComponent()).inject(this);
        this.context = context;
    }

    @Override
    public void setView(MonitorView view) {
        this.view = view;
    }

    @Override
    public Fragment setFragment(List<MinStationFullObj> listAllStation, int position) {
        for (int i = 0; i<listAllStation.size(); i++) {
            if (i == position % listAllStation.size()) {
                int stationId = listAllStation.get(i).getStationInfo().getStationId();
                if (i == 0) {
                    view.showLoading();
                    return RecyclerMonitorFragment.newInstance(stationId, new SplashPresenterImpl.GetStationCallback() {
                        @Override
                        public void callback(EStatus eStatus) {
                            view.hideLoading();
                            if (eStatus == EStatus.NETWORK_FAILURE) {
                                if (isShowAlert) {
                                    view.showAlert().show();
                                }
                                isShowAlert = false;
                            } else if (eStatus == EStatus.GET_SENSOR_OBJ_SUCCESS) {
                            }
                        }
                    });
                } else {
                    return RecyclerMonitorFragment.newInstance(stationId, new SplashPresenterImpl.GetStationCallback() {
                        @Override
                        public void callback(EStatus eStatus) {
                        }
                    });
                }
            }
        }
        return null;
    }

    @Override
    public int getCount(List<MinStationFullObj> listAllStation) {
        if (listAllStation != null) {
            return listAllStation.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(List<MinStationFullObj> listAllStation, int position) {
        if (listAllStation != null) {
            for (int i = 0; i < listAllStation.size(); i++) {
                if (i == position % listAllStation.size()) {
                    return listAllStation.get(i).getStationInfo().getStationName();
                }
            }
        }
        return "";
    }

    @Override
    public HeaderDesign getHeaderDesign(List<MinStationFullObj> listAllStation, int page) {
        switch (page) {
            case 0:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.sl_terbium_green,
                        "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
            case 1:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.sl_red_orange,
                        "http://buudienhospital.vn/wp-content/uploads/2017/04/tgd_vnpt.jpg");
//                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
            case 2:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.cyan,
                        "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
            case 3:
                return HeaderDesign.fromColorResAndUrl(
                        R.color.red,
                        "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
        }

        return HeaderDesign.fromColorResAndUrl(
                R.color.sl_terbium_green,
                "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
    }

    //**************************************************/
    //                     MQTT                        */
    //**************************************************/
    private VNPTClient vnptClient = null;
    @Override
    public void connectMQTT() {
        String broker = Utils.getBroker(context);
        BTSPreferences prefs = preferenceManager.getPreferences();
        String roleId = prefs.roleId;
        if(roleId.length() > 0 && roleId.equalsIgnoreCase(Role.Admin.getValue())) {
            try {
                vnptClient = new VNPTClient(Utils.createdRandomString(7), broker);
                vnptClient.connect(null, null, null, null);
                Log.i("0x00", "Establish MQTT conection");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            vnptClient = null;
        }
    }

    @Override
    public void subscribeToTopic(String topic, final MQTTCallback callback) {
        Log.i("0x00", "Subscribe to " + topic);
        try {
            if (vnptClient != null) {
                Log.i("0x00", "subscribe to " + topic + " success");
                vnptClient.subscribe(Utils.getTopic() + topic, new VNPTClientEventHandle() {
                    @Override
                    public void onMessageArrived(String topic, String message) {
                        callback.detectNoise(message);
                    }
                });
            } else {
                Log.i("0x00", "subscribe to " + topic + " fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribeToToptic(String topic) {
        Log.i("0x00", "Unsubscribe to " + topic);
        try {
            if (vnptClient != null) {
                vnptClient.unsubscribe(Utils.getTopic() + topic);
                vnptClient.disconnect();
                vnptClient = null;
                Log.i("0x00", "unsubscribe to " + topic + " success");
            } else {
                Log.i("0x00", "unsubscribe to " + topic + " fail");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void publicToMQTT(String topic) {

    }

    //**************************************************/
    //                     NOTIFICATION                */
    //**************************************************/
    private int id = 0;
    private String titleNotification = "Cảnh báo âm thanh";
    @Override
    public void showNotification(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String formatType = "";
            String others = "";
            if (jsonObject.has("formatType") && jsonObject.has("other")) {
                BTSPreferences prefs = preferenceManager.getPreferences();
                formatType = jsonObject.getString("formatType");
                others = jsonObject.getString("other");
                String roleId = prefs.roleId;
                if (roleId.equalsIgnoreCase(Role.Admin.getValue())) {
                    // BACKGROUND
                    if (NotificationUtils.isAppIsInBackground(context)) {
                        Intent resultIntent = new Intent(context, MonitorContainer.class);
                        resultIntent.putExtra("message", message);
                        showNotificationMessage(
                                id,
                                context,
                                titleNotification,
                                Utils.convertToTime((new Date().toString())),
                                Utils.convertToTime((new Date().toString())),
                                resultIntent
                        );
                        NotificationUtils notificationUtils = new NotificationUtils(context);
                        notificationUtils.playNotificationSound();

                    } else { // FOREGROUND
                        Intent resultIntent = new Intent(context, MonitorContainer.class);
                        resultIntent.putExtra("message", others);
                        showNotificationMessage(
                                id,
                                context,
                                titleNotification,
                                Utils.convertToTime((new Date().toString())),
                                Utils.convertToTime((new Date().toString())),
                                resultIntent
                        );
                        handleNotification(others);
                    }
                    id++;
                }

            } else {
            }

        } catch (JSONException e) {
            Log.e("0x00", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("0x00", "Exception: " + e.getMessage());
        }
    }

    private NotificationUtils notificationUtils;
    /**
     * Hiển thị notification với text
     */
    private void showNotificationMessage(int id, Context context, String title, String message, String timeStamp, Intent intent) {
        BTSPreferences prefs = preferenceManager.getPreferences();
        String roleId = prefs.roleId;
        if (roleId.equalsIgnoreCase(Role.Admin.getValue())) {
            notificationUtils = new NotificationUtils(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationUtils.showNotificationMessage(id, title, message, timeStamp, intent);
        }
    }

    /**
     * Hàm hiển thị notification theo toast khi app đang trong chế độ foregroound
     * */
    private void handleNotification(String message) {

        BTSPreferences prefs = preferenceManager.getPreferences();
        String roleId = prefs.roleId;
        if (roleId.equalsIgnoreCase(Role.Admin.getValue())) {
            // FOREGROUND
            if (!NotificationUtils.isAppIsInBackground(context)) {
                Intent pushNotification = new Intent(Utils.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", "CẢNH BÁO ÂM THANH");
                LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.playNotificationSound();
            } else {
            }
        }
    }
}
