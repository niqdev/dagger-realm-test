package com.github.niqdev.component.module;

import com.github.niqdev.service.PreferenceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class ApplicationContextModuleTest {

    @Provides
    @Singleton
    public PreferenceService providePreferenceService() {
        return mock(PreferenceService.class);
    }
}
