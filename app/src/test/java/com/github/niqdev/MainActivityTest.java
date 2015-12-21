package com.github.niqdev;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {

    @Test
    public void onClick_shouldChangeText() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        Button myButton = (Button) activity.findViewById(R.id.myButton);
        TextView myTextView = (TextView) activity.findViewById(R.id.myTextView);

        assertThat(myTextView.getText().toString(), equalTo("BEFORE"));
        myButton.performClick();
        assertThat(myTextView.getText().toString(), equalTo("AFTER"));
    }

}
