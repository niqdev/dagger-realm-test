package com.github.niqdev;

import android.app.Application;

import com.github.niqdev.component.Injector;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Injector.initializeApplicationComponent(this);
    }

}
