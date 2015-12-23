package com.github.niqdev;

import android.app.Application;

import com.github.niqdev.component.Injector;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.initializeApplicationComponent(this);
    }

}
