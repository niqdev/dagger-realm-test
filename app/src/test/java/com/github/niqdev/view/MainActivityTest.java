package com.github.niqdev.view;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.R;
import com.github.niqdev.component.ApplicationComponentTest;
import com.github.niqdev.component.DaggerApplicationComponentTest;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.component.module.RepositoryModuleTest;
import com.github.niqdev.model.MessageModel;
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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Injector.class})
public class MainActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    PreferenceService preferenceServiceMock;

    @Inject
    MessageRepository messageRepository;

    @Bind(R.id.textViewExample1)
    TextView textViewExample1;

    @Bind(R.id.editTextExample2)
    EditText editTextExample2;

    @Bind(R.id.editTextExample3Content)
    EditText editTextExample3Content;

    @Bind(R.id.editTextExample3Info)
    EditText editTextExample3Info;

    @Bind(R.id.buttonExample1)
    Button buttonExample1;

    @Bind(R.id.buttonExample2)
    Button buttonExample2;

    @Bind(R.id.buttonExample3)
    Button buttonExample3;

    @Bind(R.id.listViewExample3)
    ListView listViewExample3;

    @Before
    public void setup() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(true))
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
    }

    private void setupActivity() {
        ButterKnife.bind(this, Robolectric.setupActivity(MainActivity.class));
    }

    private void skipInitRefreshMessages() {
        when(messageRepository.findAll()).thenReturn(Observable.<List<MessageModel>>empty());
    }

    @Test
    public void onClick_shouldChangeText() {
        skipInitRefreshMessages();
        setupActivity();

        assertThat(textViewExample1.getText().toString(), equalTo("BEFORE"));
        buttonExample1.performClick();
        assertThat(textViewExample1.getText().toString(), equalTo("AFTER"));
    }

    @Test
    public void shouldInitPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        when(preferenceServiceMock.readMyPreference()).thenReturn(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        verify(preferenceServiceMock, times(1)).readMyPreference();
        assertEquals("should init preference", MY_PREFERENCE, editTextExample2.getText().toString());
    }

    @Test
    public void onClick_shouldNotEditEmptyPreference() {
        String MY_PREFERENCE = "";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        editTextExample2.setText(MY_PREFERENCE);
        buttonExample2.performClick();
        verify(preferenceServiceMock, never()).writeMyPreference(MY_PREFERENCE);
    }

    @Test
    public void onClick_shouldEditPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        skipInitRefreshMessages();
        setupActivity();

        editTextExample2.setText(MY_PREFERENCE);
        buttonExample2.performClick();
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

        assertEquals("should init adapter", 2, listViewExample3.getAdapter().getCount());
        assertEquals("should init adapter with message", "CONTENT1", listViewExample3.getAdapter().getItem(0));
        assertEquals("should init adapter with message", "CONTENT2", listViewExample3.getAdapter().getItem(1));
    }

    @Test
    public void onClick_shouldAddMessageError() {
        skipInitRefreshMessages();
        setupActivity();

        assertNull("no content error", editTextExample3Content.getError());
        editTextExample3Content.setText("");
        buttonExample3.performClick();
        assertEquals("should display error", "Required field", editTextExample3Content.getError());
    }

    @Test
    public void onClick_shouldAddMessage() {
        String MESSAGE_UUID = "UUID";
        String MESSAGE_CONTENT = "CONTENT";
        String MESSAGE_INFO = "INFO";

        skipInitRefreshMessages();
        when(messageRepository.add((MessageModel)notNull())).thenReturn(Observable.just(MESSAGE_UUID));
        setupActivity();

        editTextExample3Content.setText(MESSAGE_CONTENT);
        editTextExample3Info.setText(MESSAGE_INFO);

        buttonExample3.performClick();
        assertNull("no content error", editTextExample3Content.getError());
        assertEquals("should clear value", "", editTextExample3Content.getText().toString());
        assertEquals("should clear value", "", editTextExample3Info.getText().toString());

        verify(messageRepository).add((MessageModel)notNull());
        verify(messageRepository).findAll();
    }

}
