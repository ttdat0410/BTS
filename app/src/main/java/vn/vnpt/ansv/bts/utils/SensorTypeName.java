package vn.vnpt.ansv.bts.utils;

/**
 * Created by ANSV on 11/11/2017.
 */

/**
 * enum hiển thị mã loại cảm biến, quy định từ server
 * */
public enum SensorTypeName {
    Light      (1),
    Temperature(6),
    Humidity   (7),
    Ac         (15),
    Dc         (16),
    Door       (20),
    Smoke      (21),
    Fire       (22),
    Water      (23),
    Sound      (19),
    NH3        (31);
    public final int value;
    SensorTypeName(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
