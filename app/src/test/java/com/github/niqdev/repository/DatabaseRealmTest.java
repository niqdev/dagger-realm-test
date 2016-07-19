package com.github.niqdev.repository;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.CustomApplicationTest;
import com.github.niqdev.component.ApplicationComponentTest;
import com.github.niqdev.component.DaggerApplicationComponentTest;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.component.module.RepositoryModuleTest;
import com.github.niqdev.model.MessageModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(application = CustomApplicationTest.class, constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class, Injector.class, RealmQuery.class, RealmResults.class})
public class DatabaseRealmTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    DatabaseRealm databaseRealm;

    Realm realmMock;

    @Before
    public void setupRealm() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(true, false))
            .build();

        mockStatic(Injector.class);
        when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);

        realmMock = mock(Realm.class);
        mockStatic(Realm.class);
        when(Realm.getDefaultInstance()).thenReturn(realmMock);
    }

    @Test
    public void test_add() {
        MessageModel message = new MessageModel();

        doNothing().when(realmMock).beginTransaction();
        doNothing().when(realmMock).commitTransaction();
        databaseRealm.add(message);

        assertThat(Realm.getDefaultInstance(), is(realmMock));
        verify(realmMock).beginTransaction();
        verify(realmMock).copyToRealm(message);
        verify(realmMock).commitTransaction();
    }

    @Test
    public void test_findAll() {
        MessageModel message1 = new MessageModel();
        MessageModel message2 = new MessageModel();
        List<MessageModel> messageList = Arrays.asList(message1, message2);
        RealmResults<MessageModel> messages = mockRealmResults();

        RealmQuery<MessageModel> query = mockRealmQuery();
        when(realmMock.where(MessageModel.class)).thenReturn(query);
        when(query.findAll()).thenReturn(messages);
        when(messages.iterator()).thenReturn(messageList.iterator());
        when(messages.size()).thenReturn(messageList.size());

        List<MessageModel> result = databaseRealm.findAll(MessageModel.class);

        assertThat(Realm.getDefaultInstance(), is(realmMock));
        verify(realmMock.where(MessageModel.class), times(1)).findAll();
        assertEquals("invalid size", 2, result.size());
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }

}

