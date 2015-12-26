package com.github.niqdev.component.module;

import com.github.niqdev.service.PreferenceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Module
public class ApplicationContextModuleTest {

    public static final String MY_PREFERENCE_MOCK = "com.github.niqdev.MAIN.MY_PREFERENCE_MOCK";

    @Provides
    @Singleton
    public PreferenceService providePreferenceService() {
        PreferenceService preferenceServiceMock = mock(PreferenceService.class);

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE_MOCK);
        when(preferenceServiceMock.readMyPreference()).thenReturn(MY_PREFERENCE_MOCK);

        return preferenceServiceMock;
    }
}
