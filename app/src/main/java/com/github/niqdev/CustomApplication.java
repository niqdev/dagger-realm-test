package com.github.niqdev;

import android.app.Application;

import com.github.niqdev.component.Injector;
import com.github.niqdev.repository.DatabaseRealm;

import javax.inject.Inject;

public class CustomApplication extends Application {

    @Inject
    DatabaseRealm databaseRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
        databaseRealm.setup();
    }

}
