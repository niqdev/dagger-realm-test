package com.github.niqdev.service.impl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.niqdev.service.PreferenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferenceServiceImpl implements PreferenceService {

    private static final Logger log = LoggerFactory.getLogger(PreferenceService.class);

    private static final String PREFERENCE_MAIN = "com.github.niqdev.MAIN";
    private static final String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

    private final SharedPreferences sharedPreferences;

    public PreferenceServiceImpl(Application application) {
        this.sharedPreferences = application.getSharedPreferences(PREFERENCE_MAIN, Context.MODE_PRIVATE);
    }

    @Override
    public String readMyPreference() {
        log.debug("readMyPreference");
        return sharedPreferences.getString(MY_PREFERENCE, "");
    }

    @Override
    public void writeMyPreference(String value) {
        log.debug("writeMyPreference: {}", value);
        sharedPreferences.edit().putString(MY_PREFERENCE, value).apply();
    }
}
