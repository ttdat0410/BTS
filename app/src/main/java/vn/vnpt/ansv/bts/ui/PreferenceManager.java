package vn.vnpt.ansv.bts.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import vn.vnpt.ansv.bts.common.injection.qualifier.ForApplication;

/**
 * Created by ANSV on 11/9/2017.
 */

@Singleton
public class PreferenceManager {

    private static final String PREFERENCES_KEY = "Bts";
    private static final String PREFERENCES_CONTENT = PreferenceManager.class.getSimpleName() + "preferences";

    private final SharedPreferences sharedPreferences;
    private final Locale locale;

    private BTSPreferences preferences;

    @Inject
    public PreferenceManager(Context context) {
        this.locale = context.getResources().getConfiguration().locale;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.preferences = getPreferences();
    }

    public BTSPreferences getPreferences() {
        if (preferences != null) {
            return preferences;
        } else {
            String jsonString = sharedPreferences.getString(PREFERENCES_CONTENT, null);
            if (jsonString == null) {
                return new BTSPreferences(locale);
            } else {
                return new Gson().fromJson(jsonString, BTSPreferences.class);
            }
        }
    }

    public void setPreferences(BTSPreferences preferences) {
        this.preferences = preferences;
        sharedPreferences.edit().putString(PREFERENCES_CONTENT, new Gson().toJson(preferences)).commit();
    }

    public void clear() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().commit();
        }
    }

    public void addConnected(String deviceAddress, String deviceName) {
        if (preferences.beacons == null) {
            preferences.beacons = new HashMap<>();
        }
        if (!preferences.beacons.containsKey(deviceAddress)) {
            preferences.beacons.put(deviceAddress, new BTSPreferences.Beacon(deviceAddress, deviceName, false));
            String jsonString = new Gson().toJson(preferences);
            sharedPreferences.edit().putString(PREFERENCES_CONTENT, jsonString).apply();
        }
    }
}
