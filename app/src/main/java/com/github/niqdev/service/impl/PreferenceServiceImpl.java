package com.github.niqdev.service.impl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.niqdev.component.Injector;
import com.github.niqdev.service.PreferenceService;

import javax.inject.Inject;

public class PreferenceServiceImpl implements PreferenceService {

    private static final String PREFERENCE_MAIN = "com.github.niqdev.MAIN";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceServiceImpl(Application application) {
        Injector.getApplicationComponent().inject(this);
        this.sharedPreferences = application.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    @Override
    public String readMyPreference() {
        // TODO
        return null;
    }

    @Override
    public void saveMyPreference() {
        // TODO
    }
}
