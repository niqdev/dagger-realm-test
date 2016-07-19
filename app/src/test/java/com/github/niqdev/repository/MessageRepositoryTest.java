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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(application = CustomApplicationTest.class, constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest({Injector.class})
public class MessageRepositoryTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    MessageRepository messageRepository;

    @Inject
    DatabaseRealm databaseRealm;

    @Before
    public void setupDagger() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(false, true))
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
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

        when(databaseRealm.add(message)).thenReturn(message);

        TestSubscriber<String> tester = new TestSubscriber<>();
        messageRepository.add(message).subscribe(tester);

        verify(databaseRealm).add(message);

        tester.assertValue(MESSAGE_UUID);
        tester.assertCompleted();
        tester.assertNoErrors();
    }

    @Test
    public void messageRepository_findAll() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();
        List<MessageModel> messages = Arrays.asList(message1, message2);

        when(databaseRealm.findAll(MessageModel.class)).thenReturn(messages);

        TestSubscriber<List<MessageModel>> tester = new TestSubscriber<>();
        messageRepository.findAll().subscribe(tester);

        tester.assertValue(messages);
        tester.assertCompleted();
        tester.assertNoErrors();
    }

}
