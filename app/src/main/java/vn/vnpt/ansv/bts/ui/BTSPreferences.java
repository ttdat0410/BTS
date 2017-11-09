package vn.vnpt.ansv.bts.ui;

/**
 * Created by ANSV on 11/9/2017.
 */

public class BTSPreferences {


    public String userName;
    public String password;
    public String apiKey;
    public String userId;


    public BTSPreferences() {

    }

    @Override
    public String toString() {
        return String.format("userName: %s, password: %s, apiKey: %s, userId: %s",
                userName, password, apiKey, userId);
    }

}
