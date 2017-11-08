package vn.vnpt.ansv.bts.utils;

/**
 * Created by ANSV on 11/8/2017.
 */

public enum StatusServer {
    Success(1),
    ObjectAlreadyExists(2),
    PermissionDenied(5),
    InvalidParameter(6),
    InvalidApiKey(7),
    UsernameOrPasswordIsIncorrect(9),
    Failure(0x9999);

    public final int value;
    StatusServer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

