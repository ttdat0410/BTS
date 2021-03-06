package vn.vnpt.ansv.bts.utils;

/**
 * Created by ANSV on 11/9/2017.
 */

/**
 * enum hiên thị các trạng thái view khi có action hoặc sự kiện
 * */
public enum EStatus {
    NETWORK_FAILURE,
    USERNAME_IS_EMPTY,
    PASSWORD_IS_EMPTY,
    USERNAME_INCORRECT,
    PASSWORD_INCORRECT,
    UESRNAME_UNMATCHED_WITH_FORMAT,
    PASSWORD_UNMATCHED_WITH_FORMAT,
    CHECKING_ACCOUNT,
    LOGIN,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    APIKEY_INVAILABLE,
    USERID_INVAILABLE,
    GET_STATIONS_SUCCESS,
    GET_STATIONS_FAILURE,
    GET_SENSOR_OBJ_SUCCESS;
}
