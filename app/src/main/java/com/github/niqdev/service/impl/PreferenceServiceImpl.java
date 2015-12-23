package com.github.niqdev.service.impl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.niqdev.service.PreferenceService;

public class PreferenceServiceImpl implements PreferenceService {

    private static final String PREFERENCE_MAIN = "com.github.niqdev.MAIN";

    private final SharedPreferences sharedPreferences;

    public PreferenceServiceImpl(Application application) {
        this.sharedPreferences = application.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    @Override
    public String readMyPreference() {
        // TODO
        return null;
    }

    @Override
    public void saveMyPreference(String value) {
        // TODO
    }
}
