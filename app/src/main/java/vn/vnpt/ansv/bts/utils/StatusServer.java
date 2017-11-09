package vn.vnpt.ansv.bts.utils;

/**
 * Created by ANSV on 11/8/2017.
 */

public enum StatusServer {
    Success(1),
    ObjectAlreadyExists(2),
    ObjectDoesNotExist(3),
    DataNotFound(4),
    PermissionDenied(5),
    InvalidParameter(6),
    InvalidApiKey(7),
    ApiKeyNotFound(8),
    UsernameOrPasswordIsIncorrect(9),
    SystemError(10),
    ActionIsBlocked(11),
    InvalidAction(12),
    Failure(0x9999);

    public final int value;
    StatusServer(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

