package com.github.niqdev.view;

import android.widget.Button;
import android.widget.TextView;

import com.github.niqdev.BuildConfig;
import com.github.niqdev.R;
import com.github.niqdev.component.ApplicationComponentTest;
import com.github.niqdev.component.DaggerApplicationComponentTest;
import com.github.niqdev.component.Injector;
import com.github.niqdev.component.module.ApplicationContextModuleTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PrepareForTest({Injector.class})
public class MainActivityTest {

    @Before
    public void setup() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .build();

        // TODO
        PowerMockito.mockStatic(Injector.class);
//        /Mockito.doNothing().when(Injector.class).
        Mockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);
    }

    @Test
    public void onClick_shouldChangeText() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        Button myButton = (Button) activity.findViewById(R.id.buttonExample1);
        TextView myTextView = (TextView) activity.findViewById(R.id.textViewExample1);

        assertThat(myTextView.getText().toString(), equalTo("BEFORE"));
        myButton.performClick();
        assertThat(myTextView.getText().toString(), equalTo("AFTER"));
    }

    @Test
    public void onClick_shouldEditPreference() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
    }

}
