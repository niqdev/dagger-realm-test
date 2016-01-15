package com.github.niqdev.repository;

import com.github.niqdev.BuildConfig;
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
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
//@PowerMockIgnore({"io.realm.Realm", "io.realm.RealmQuery", "io.realm.RealmResults"})
//@PrepareForTest({Injector.class, Realm.class, RealmQuery.class, RealmResults.class})
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest({Injector.class, Realm.class, RealmResults.class})
public class MessageRepositoryTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    MessageRepository messageRepository;

    @Inject
    DatabaseHelper databaseHelper;

    Realm realmMock;

    @Before
    public void setupDagger() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(false))
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
    }

    @Before
    public void setupRealm() {
        realmMock = PowerMockito.mock(Realm.class);
        PowerMockito.mockStatic(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(realmMock);
        doNothing().when(realmMock).beginTransaction();
        doNothing().when(realmMock).commitTransaction();
    }

    @Test
    public void messageRepository_add() {
        String MESSAGE_UUID = "UUID";
        String MESSAGE_CONTENT = "CONTENT";
        String MESSAGE_INFO = "INFO";
        MessageModel message = new MessageModel();
        message.setUuid(MESSAGE_UUID);
        message.setContent(MESSAGE_CONTENT);
        message.setInfo(MESSAGE_INFO);

        when(databaseHelper.getRealmInstance()).thenReturn(realmMock);

        TestSubscriber<String> tester = new TestSubscriber<>();
        messageRepository.add(message).subscribe(tester);

        assertThat(Realm.getDefaultInstance(), is(realmMock));
        verify(realmMock).beginTransaction();
        verify(realmMock).copyToRealm(message);
        verify(realmMock).commitTransaction();

        tester.assertValue(MESSAGE_UUID);
        tester.assertCompleted();
        tester.assertNoErrors();
    }

    /*
    @Test
    public void messageRepository_findAll() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();
        List<MessageModel> messages = Arrays.asList(message1, message2);

        MessageRepository messageRepositoryMock = mock(MessageRepository.class);
        when(messageRepositoryMock.findAll()).thenReturn(Observable.just(messages));

        TestSubscriber<List<MessageModel>> tester = new TestSubscriber<>();
        messageRepositoryMock.findAll().subscribe(tester);

        tester.assertValue(messages);
        tester.assertCompleted();
        tester.assertNoErrors();
    }
    @Test
    public void messageRepository_findAll() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();
        List<MessageModel> messages =  Arrays.asList(message1, message2);

        //RealmResults<MessageModel> realmObjects;
        //PowerMockito.doReturn(realmObjects).when(RealmResults.class, "createFromDynamicClass", realmMock, query.findAll(), MessageModel.class);

        query = PowerMockito.mock(RealmQuery.class);
        //PowerMockito.mockStatic(RealmQuery.class);
        when(Realm.getDefaultInstance().where(MessageModel.class)).thenReturn(query);

        //when(query.findAll()).thenReturn((RealmResults) messages);

        RealmResults<MessageModel> realmResults;
        try {
            realmResults = Whitebox.invokeMethod(RealmResults.class, "createFromTableOrView", realmMock, query, MessageModel.class);
            //realmResults = Whitebox.invokeConstructor(RealmResults.class, realmMock, "MessageModel");
            realmResults.addAll(2, messages);
            when(query.findAll()).thenReturn(realmResults);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // TODO
        //RealmResults<MessageModel> realmObjects = RealmResults.createFromDynamicClass(realmMock, query.findAll(), MessageModel.class);
        //realmObjects.addAll(2, messages);
        //when(query.findAll()).thenReturn(realmObjects);

        TestSubscriber <List<MessageModel>> tester = new TestSubscriber<>();
        messageRepository.findAll().subscribe(tester);

        verify(query).findAll();

        tester.assertValue(messages);
        tester.assertCompleted();
        tester.assertNoErrors();
    }
    */

    @Test
    public void messageRepository_findAll() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();

//        try {
//            PowerMockito.doReturn(message1).when(realmMock, "get", MessageModel.class, 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        when(databaseHelper.getRealmInstance()).thenReturn(realmMock);

        RealmQuery<MessageModel> realmQuery = mock(RealmQuery.class);
        when(realmMock.where(MessageModel.class)).thenReturn(realmQuery);

        RealmResults<MessageModel> realmResults = mock(RealmResults.class);
        when(realmQuery.findAll()).thenReturn(realmResults);

        TestSubscriber<List<MessageModel>> tester = new TestSubscriber<>();
        messageRepository.findAll().subscribe(tester);

        verify(realmQuery).findAll();

        tester.assertValue(realmResults);
        tester.assertCompleted();
        tester.assertNoErrors();
    }
}
