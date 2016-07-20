package com.github.niqdev.view;

import android.app.Activity;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.CustomApplicationTest;
import com.github.niqdev.component.ApplicationComponentTest;
import com.github.niqdev.component.DaggerApplicationComponentTest;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.component.module.RepositoryModuleTest;
import com.github.niqdev.model.MessageModel;
import com.github.niqdev.repository.DatabaseRealm;
import com.github.niqdev.repository.MessageRepository;
import com.github.niqdev.service.PreferenceService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(application = CustomApplicationTest.class, constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.robolectric.*", "android.*"})
@PrepareForTest({Injector.class})
public class MainActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    PreferenceService preferenceServiceMock;

    @Inject
    MessageRepository messageRepository;

    @Inject
    DatabaseRealm databaseRealm;

    MainActivity activity;

    @Before
    public void setup() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(true, true))
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
    }

    private void setupActivity() {
        ButterKnife.setDebug(true);
        activity = Robolectric.setupActivity(MainActivity.class);
        ButterKnife.bind(this, activity);
    }

    private void skipInitRefreshMessages() {
        when(messageRepository.findAll()).thenReturn(Observable.<List<MessageModel>>empty());
    }

    @Test
    public void onClick_shouldChangeText() {
        skipInitRefreshMessages();
        setupActivity();

        assertThat(activity.textViewExample1.getText().toString(), equalTo("BEFORE"));
        activity.buttonExample1.performClick();
        assertThat(activity.textViewExample1.getText().toString(), equalTo("AFTER"));
    }

    @Test
    public void shouldInitPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        when(preferenceServiceMock.readMyPreference()).thenReturn(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        verify(preferenceServiceMock, times(1)).readMyPreference();
        assertEquals("should init preference", MY_PREFERENCE, activity.editTextExample2.getText().toString());
    }

    @Test
    public void onClick_shouldNotEditEmptyPreference() {
        String MY_PREFERENCE = "";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        activity.editTextExample2.setText(MY_PREFERENCE);
        activity.buttonExample2.performClick();
        verify(preferenceServiceMock, never()).writeMyPreference(MY_PREFERENCE);
    }

    @Test
    public void onClick_shouldEditPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        activity.editTextExample2.setText(MY_PREFERENCE);
        activity.buttonExample2.performClick();
        verify(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
    }

    @Test
    public void init_refreshMessages() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();
        List<MessageModel> messages = Arrays.asList(message1, message2);

        when(messageRepository.findAll()).thenReturn(Observable.just(messages));
        setupActivity();
        verify(messageRepository).findAll();

        assertEquals("should init adapter", 2, activity.listViewExample3.getAdapter().getCount());
        assertEquals("should init adapter with message", "CONTENT1", activity.listViewExample3.getAdapter().getItem(0));
        assertEquals("should init adapter with message", "CONTENT2", activity.listViewExample3.getAdapter().getItem(1));
    }

    @Test
    public void onClick_shouldAddMessageError() {
        skipInitRefreshMessages();
        setupActivity();

        assertNull("no content error", activity.editTextExample3Content.getError());
        activity.editTextExample3Content.setText("");
        activity.buttonExample3.performClick();
        assertEquals("should display error", "Required field", activity.editTextExample3Content.getError());
    }

    @Test
    public void onClick_shouldAddMessage() {
        String MESSAGE_UUID = "UUID";
        String MESSAGE_CONTENT = "CONTENT";
        String MESSAGE_INFO = "INFO";

        skipInitRefreshMessages();
        when(messageRepository.add((MessageModel)notNull())).thenReturn(Observable.just(MESSAGE_UUID));
        setupActivity();

        activity.editTextExample3Content.setText(MESSAGE_CONTENT);
        activity.editTextExample3Info.setText(MESSAGE_INFO);

        activity.buttonExample3.performClick();
        assertNull("no content error", activity.editTextExample3Content.getError());
        assertEquals("should clear value", "", activity.editTextExample3Content.getText().toString());
        assertEquals("should clear value", "", activity.editTextExample3Info.getText().toString());

        verify(messageRepository).add((MessageModel)notNull());
        verify(messageRepository).findAll();
    }

    @Test
    public void onCloseRealm() {
        doNothing().when(databaseRealm).close();
        skipInitRefreshMessages();

        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        Activity myActivity = controller
            .create()
            .start()
            .resume()
            .visible()
            .get();
        ButterKnife.bind(this, myActivity);

        controller
            .pause()
            .stop()
            .destroy();
        verify(databaseRealm).close();
    }

}
