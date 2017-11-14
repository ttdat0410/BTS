package vn.vnpt.ansv.bts.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.johnpersano.supertoasts.SuperToast;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Locale;

import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;

/**
 * Created by ANSV on 11/8/2017.
 */

public class Utils {

    public static SuperToast.Animations TOAST_ANIMATION = SuperToast.Animations.FLYIN;
    enum StatusDevice {
        OFF(1),
        ON(2);
        public final int value;
        StatusDevice(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public static String getBaseUrl(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        BTSPreferences prefs = preferenceManager.getPreferences();
        String ip = prefs.ip;
        String port = prefs.port;
        String baseUrl = "http://" + ip +":"+ port + "/BTSRestWebService/";
        return baseUrl;
    }

    public static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    /**
     * Hàm kiểm tra trạng thái mạng
     * @param context đối tượng sử dụng
     * @return boolean
     * */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Hàm xét icon cho dung lượng pin dựa trên giá trị pin
     * @param batteryValue giá trị pin
     * @return int
     * */
    public static int setBatteryImageView(int batteryValue) {
        if (batteryValue < 5) {
            return R.mipmap.battery_0bar;
        } else if (batteryValue >= 5 && batteryValue < 25) {
            return R.mipmap.battery_1bar;
        } else if (batteryValue >= 25 && batteryValue < 50) {
            return R.mipmap.battery_2bar;
        } else if (batteryValue >= 50 && batteryValue < 75) {
            return R.mipmap.battery_3bar;
        } else if (batteryValue >= 75) {
            return R.mipmap.battery_4bar;
        } else {
            return R.mipmap.battery_0bar;
        }
    }

    /**
     * Hàm xét màu cho giá trị của pin dựa vào giá trị pin
     * @param batteryValue giá trị pin.
     * @return  int
     * */
    public static int setColorForBatteryValue(int batteryValue) {
        if (batteryValue < 5) {
            return R.color.sl_red_orange;
        } else {
            return R.color.sl_footer_grey;
        }
    }

    /**
     * Hàm xét màu cho giá trị của cảm biến dựa vào trang thái bật tắt của cảm biến
     * @param statusId trạng thái bật tắt của cảm biến
     * @return int
     * */
    public static int setColorForSensorValue(int statusId) {
        if (statusId == 2) {
            return R.color.sl_terbium_green;
        } else {
            return R.color.sl_terbium_green;
        }
    }

    /**
     * Hàm xét icon cho cảm biến dựa vào trang thái bật tắt của cảm biến và mã loại cảm biến
     * @param statusId trạng thái bật tắt của cảm biến
     * @param sensorTypeId mã loại cảm biến
     * @return int
     * */
    public static int setSensorIconImageView(int statusId, int sensorTypeId) {

        if (statusId == StatusDevice.OFF.getValue()) {
            if (sensorTypeId == SensorTypeName.Light.getValue()) {
                return R.mipmap.ic_light_inactive;
            } else if (sensorTypeId == SensorTypeName.Temperature.getValue()) {
                return R.mipmap.ic_temp_inactive;
            } else if (sensorTypeId == SensorTypeName.Humidity.getValue()) {
                return R.mipmap.ic_humidity_inactive;
            } else if (sensorTypeId == SensorTypeName.Ac.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Dc.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Door.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Smoke.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Fire.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Water.getValue()) {
                return R.mipmap.ic_tint_background;
            } else if (sensorTypeId == SensorTypeName.Sound.getValue()) {
                return R.mipmap.ic_sound_inactive;
            } else if (sensorTypeId == SensorTypeName.NH3.getValue()) {
                return R.mipmap.ic_nh3_inactive;
            } else {
                return R.mipmap.power_off;
            }
        } else {
            if (sensorTypeId == SensorTypeName.Light.getValue()) {
                return R.mipmap.ic_light_active;
            } else if (sensorTypeId == SensorTypeName.Temperature.getValue()) {
                return R.mipmap.ic_temp_active;
            } else if (sensorTypeId == SensorTypeName.Humidity.getValue()) {
                return R.mipmap.ic_humidity_active;
            } else if (sensorTypeId == SensorTypeName.Ac.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Dc.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Door.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Smoke.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Fire.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Water.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Sound.getValue()) {
                return R.mipmap.ic_sound_active;
            } else if (sensorTypeId == SensorTypeName.NH3.getValue()) {
                return R.mipmap.ic_nh3_active;
            } else {
                return R.mipmap.power_on;
            }
        }
    }

    /**
     * Hàm so sánh, sắp xếp List theo sensorTypeId
     * */
    public static Comparator<MinSensorFullObj> comparatorWithSensorTypeId = new Comparator<MinSensorFullObj>() {
        @Override
        public int compare(MinSensorFullObj lhs, MinSensorFullObj rhs) {
            int sensorTypeId1 = lhs.getSensorInfo().getSensorTypeId();
            int sensorTypeId2 = rhs.getSensorInfo().getSensorTypeId();
            return sensorTypeId1-sensorTypeId2;
        }
    };

    /**
     * Hàm so sánh, sắp xếp List theo sensorName
     * */
    public static Comparator<MinSensorFullObj> comparatorWithSensorName = new Comparator<MinSensorFullObj>() {
        @Override
        public int compare(MinSensorFullObj lhs, MinSensorFullObj rhs) {
            Locale locale = Locale.ENGLISH;
            String sensorName1 = lhs.getSensorInfo().getSensorName().toUpperCase(locale);
            String sensorName2 = rhs.getSensorInfo().getSensorName().toUpperCase(locale);
            return sensorName1.compareTo(sensorName2);
        }
    };
}
