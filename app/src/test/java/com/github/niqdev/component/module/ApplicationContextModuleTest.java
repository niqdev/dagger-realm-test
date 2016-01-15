package com.github.niqdev.component.module;

import android.content.Context;

import com.github.niqdev.service.PreferenceService;

import org.robolectric.RuntimeEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class ApplicationContextModuleTest {

    @Provides
    @Singleton
    public Context applicationContext() {
        return RuntimeEnvironment.application.getApplicationContext();
    }

    @Provides
    @Singleton
    public PreferenceService providePreferenceService() {
        return mock(PreferenceService.class);
    }
}
