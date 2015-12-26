package com.github.niqdev.view;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.R;
import com.github.niqdev.component.ApplicationComponentTest;
import com.github.niqdev.component.DaggerApplicationComponentTest;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

    @Inject
    PreferenceService preferenceServiceMock;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Bind(R.id.textViewExample1)
    TextView textViewExample1;

    @Bind(R.id.editTextExample2)
    EditText editTextExample2;

    @Bind(R.id.buttonExample1)
    Button buttonExample1;

    @Bind(R.id.buttonExample2)
    Button buttonExample2;

    @Before
    public void setup() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
    }

    private void setupActivity() {
        ButterKnife.bind(this, Robolectric.setupActivity(MainActivity.class));
    }

    @Test
    public void onClick_shouldChangeText() {
        setupActivity();

        assertThat(textViewExample1.getText().toString(), equalTo("BEFORE"));
        buttonExample1.performClick();
        assertThat(textViewExample1.getText().toString(), equalTo("AFTER"));
    }

    @Test
    public void onClick_shouldInitPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        when(preferenceServiceMock.readMyPreference()).thenReturn(MY_PREFERENCE);
        setupActivity();

        verify(preferenceServiceMock, times(1)).readMyPreference();
        assertEquals("should init preference", MY_PREFERENCE, editTextExample2.getText().toString());
    }

    @Test
    public void onClick_shouldNotEditEmptyPreference() {
        String MY_PREFERENCE = "";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        setupActivity();

        editTextExample2.setText(MY_PREFERENCE);
        buttonExample2.performClick();
        verify(preferenceServiceMock, never()).writeMyPreference(MY_PREFERENCE);
    }

    @Test
    public void onClick_shouldEditPreference() {
        String MY_PREFERENCE = "com.github.niqdev.MAIN.MY_PREFERENCE";

        doNothing().when(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
        setupActivity();

        editTextExample2.setText(MY_PREFERENCE);
        buttonExample2.performClick();
        verify(preferenceServiceMock).writeMyPreference(MY_PREFERENCE);
    }

}
