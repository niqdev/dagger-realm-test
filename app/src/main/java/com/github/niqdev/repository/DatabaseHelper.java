package com.github.niqdev.repository;

import android.content.Context;

import com.github.niqdev.component.Injector;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

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

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }
}
