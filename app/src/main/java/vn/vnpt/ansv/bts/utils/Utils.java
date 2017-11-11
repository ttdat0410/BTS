package vn.vnpt.ansv.bts.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.johnpersano.supertoasts.SuperToast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vn.vnpt.ansv.bts.R;

/**
 * Created by ANSV on 11/8/2017.
 */

public class Utils {

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

    public static final String BASE_URL = "http://10.4.1.204:8081/BTSRestWebService/";
//    public static final String BASE_URL = "http://113.161.61.89:40081/BTSRestWebService/";
    public static SuperToast.Animations TOAST_ANIMATION = SuperToast.Animations.FLYIN;

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

    public static int setColorForBatteryValue(int batteryValue) {
        if (batteryValue < 5) {
            return R.color.sl_red_orange;
        } else {
            return R.color.sl_footer_grey;
        }
    }

    public static int setColorForSensorValue(int stationId) {
        if (stationId == 2) {
            return R.color.sl_terbium_green;
        } else {
            return R.color.sl_terbium_green;
        }
    }

    public static int setSensorIconImageView(int stationId, int sensorTypeId) {

        if (stationId == StatusDevice.OFF.getValue()) {
            if (sensorTypeId == SensorTypeName.Light.getValue()) {
                return R.mipmap.ic_light_inactive;
            } else if (sensorTypeId == SensorTypeName.Temperature.getValue()) {
                return R.mipmap.ic_temp_inactive;
            } else if (sensorTypeId == SensorTypeName.Humidity.getValue()) {
                return R.mipmap.ic_humidity_inactive;
            } else if (sensorTypeId == SensorTypeName.Ac.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Dc.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Door.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Smoke.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Fire.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Water.getValue()) {
                return R.mipmap.ic_lights_on_red;
            } else if (sensorTypeId == SensorTypeName.Sound.getValue()) {
                return R.mipmap.ic_sound_level_inactive;
            } else if (sensorTypeId == SensorTypeName.NH3.getValue()) {
                return R.mipmap.ic_nh3_inactive;
            } else {
                return R.mipmap.ic_lights_on_green;
            }
        } else {
            if (sensorTypeId == SensorTypeName.Light.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Temperature.getValue()) {
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.Humidity.getValue()) {
                return R.mipmap.ic_motion;
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
                return R.mipmap.ic_motion;
            } else if (sensorTypeId == SensorTypeName.NH3.getValue()) {
                return R.mipmap.ic_motion;
            } else {
                return R.mipmap.ic_motion;
            }
        }
    }
}
