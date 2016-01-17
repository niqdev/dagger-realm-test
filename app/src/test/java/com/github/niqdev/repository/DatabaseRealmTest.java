package com.github.niqdev.repository;

import com.github.niqdev.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.realm.Realm;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest({Realm.class})
public class DatabaseRealmTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setupRealm() {
        Realm realmMock = PowerMockito.mock(Realm.class);
        PowerMockito.mockStatic(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(realmMock);
        doNothing().when(realmMock).beginTransaction();
        doNothing().when(realmMock).commitTransaction();
    }

    @Test
    public void test_add() {
        // TODO
        /*
        assertThat(Realm.getDefaultInstance(), is(realmMock));
        verify(realmMock).beginTransaction();
        verify(realmMock).copyToRealm(environment);
        verify(realmMock).commitTransaction();
        */
    }

}
