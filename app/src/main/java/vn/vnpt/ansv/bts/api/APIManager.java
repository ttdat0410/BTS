package vn.vnpt.ansv.bts.api;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import vn.vnpt.ansv.bts.common.injection.qualifier.ForApplication;

/**
 * Created by ANSV on 11/8/2017.
 */
@Singleton
public class APIManager {

    private final Context context;
    private final String restApiIp;
    private final String restApiPort;

    @Inject
    public APIManager(
            @ForApplication Context context,
            @ForApplication String restApiIp,
            @ForApplication String restApiPort ) {
        this.context = context;
        this.restApiIp = restApiIp;
        this.restApiPort = restApiPort;
    }

}
