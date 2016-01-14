package com.github.niqdev;

import android.app.Application;

import com.github.niqdev.component.Injector;
import com.github.niqdev.repository.DatabaseHelper;

import javax.inject.Inject;

public class CustomApplication extends Application {

    @Inject
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
        databaseHelper.setup();
    }

}
