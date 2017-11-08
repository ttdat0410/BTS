package vn.vnpt.ansv.bts.common.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vn.vnpt.ansv.bts.common.injection.qualifier.ForApplication;

/**
 * Created by ANSV on 11/7/2017.
 */

@Module
public class BTSModule {
    private final Context context;
    private final String restApiIp;
    private final String restApiPort;

    public BTSModule(Context context, String restApiIp, String restApiPort) {
        this.context = context;
        this.restApiIp = restApiIp;
        this.restApiPort = restApiPort;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    @ForApplication
    String provideRestApiIp() {
        return restApiIp;
    }

    @Provides
    @Singleton
    @ForApplication
    String provideRestApiPort() {
        return restApiPort;
    }

}
