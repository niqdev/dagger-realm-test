package com.github.niqdev.repository;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.component.ApplicationComponent;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.model.MessageModel;

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

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class, Injector.class})
public class DatabaseRealmTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    DatabaseRealm databaseRealm;

    Realm realmMock;

    @Singleton
    @Component(modules = {
        ApplicationContextModuleTest.class,
        DatabaseModuleTest.class
    })
    public interface DatabaseComponentTest extends ApplicationComponent {
        void inject(DatabaseRealmTest database);
    }


    @Module
    public class DatabaseModuleTest {

        @Provides
        @Singleton
        public MessageRepository provideMessageRepository() {
            return mock(MessageRepository.class);
        }

        @Provides
        @Singleton
        public DatabaseRealm provideDatabaseHelper() {
            return new DatabaseRealm();
        }
    }

    @Before
    public void setupRealm() {
        DatabaseComponentTest applicationComponentTest = DaggerDatabaseRealmTest_DatabaseComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .databaseModuleTest(new DatabaseModuleTest())
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((DatabaseComponentTest) Injector.getApplicationComponent()).inject(this);

        realmMock = PowerMockito.mock(Realm.class);
        PowerMockito.mockStatic(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(realmMock);
        doNothing().when(realmMock).beginTransaction();
        doNothing().when(realmMock).commitTransaction();
    }

    @Test
    public void test_add() {
        MessageModel message = new MessageModel();

        databaseRealm.add(message);

        assertThat(Realm.getDefaultInstance(), is(realmMock));
        verify(realmMock).beginTransaction();
        verify(realmMock).copyToRealm(message);
        verify(realmMock).commitTransaction();
    }

    @Test
    public void test_findAll() {
//        MessageModel message1 = new MessageModel();
//        MessageModel message2 = new MessageModel();
//        List<MessageModel> messages = new ArrayList<>();
//        messages.add(message1);
//        messages.add(message2);
//
//        assertThat(Realm.getDefaultInstance(), is(realmMock));
//        RealmQuery<MessageModel> query = RealmQuery.createQuery(realmMock, MessageModel.class);
//        when(realmMock.where(MessageModel.class)).thenReturn(query);
//        when(query.findAll()).thenReturn((RealmResults)messages);
//
//        List<MessageModel> result = databaseRealm.findAll(MessageModel.class);
    }

}

