package com.github.niqdev.service.impl;

import android.app.Application;
import android.content.SharedPreferences;

import com.github.niqdev.service.PreferenceService;

public class PreferenceServiceImpl implements PreferenceService {

    private static final String PREFERENCE_MAIN = "com.github.niqdev.MAIN";

    private final SharedPreferences sharedPreferences;

    //@Inject
    public PreferenceServiceImpl(Application application) {
        throw new IllegalStateException("missing application component inject");

        //this.sharedPreferences = application.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }
}
