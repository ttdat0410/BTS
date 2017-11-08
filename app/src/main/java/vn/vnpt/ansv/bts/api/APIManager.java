package vn.vnpt.ansv.bts.api;

import android.content.Context;

import javax.inject.Inject;

import vn.vnpt.ansv.bts.common.injection.qualifier.ForActivity;
import vn.vnpt.ansv.bts.common.injection.qualifier.ForApplication;

/**
 * Created by ANSV on 11/8/2017.
 */

public class APIManager {

    private Context context;
    private String restIp;
    private String restPort;

    @Inject
    public APIManager(
            @ForApplication Context context,
            @ForActivity String restIp,
            @ForActivity String restPort ) {
        this.context = context;
        this.restIp = restIp;
        this.restPort = restPort;
    }

}
