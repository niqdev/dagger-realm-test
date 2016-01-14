package com.github.niqdev.repository;

import android.content.Context;

import com.github.niqdev.component.Injector;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DatabaseHelper {

    @Inject
    Context mContext;

    RealmConfiguration realmConfiguration;

    public DatabaseHelper() {
        Injector.getApplicationComponent().inject(this);
    }

    public void setup() {
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration.Builder(mContext).build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }
}
