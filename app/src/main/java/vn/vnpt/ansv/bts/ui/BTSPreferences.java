package vn.vnpt.ansv.bts.ui;

/**
 * Created by ANSV on 11/9/2017.
 */

public class BTSPreferences {

    public String userName;
    public String password;
    public String apiKey;
    public String userId;
    public String ip;
    public String port;
    public String roleId;

    public BTSPreferences() {
    }

    @Override
    public String toString() {
        return String.format("ip: %s, port: %s, userName: %s, password: %s, apiKey: %s, userId: %s, roleId: %s",
                ip, port, userName, password, apiKey, userId, roleId);
    }
}
