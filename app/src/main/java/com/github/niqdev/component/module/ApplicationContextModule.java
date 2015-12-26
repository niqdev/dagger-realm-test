package com.github.niqdev.component.module;

import android.content.Context;

import com.github.niqdev.CustomApplication;
import com.github.niqdev.service.PreferenceService;
import com.github.niqdev.service.impl.PreferenceServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationContextModule {

    private final CustomApplication application;

    public ApplicationContextModule(CustomApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public CustomApplication application() {
        return application;
    }

    @Provides
    @Singleton
    public Context applicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public PreferenceService providePreferenceService() {
        return new PreferenceServiceImpl(application);
    }

}
