package com.github.niqdev.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.niqdev.R;
import com.github.niqdev.component.Injector;
import com.github.niqdev.service.PreferenceService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindString(R.string.text_after)
    String textAfter;

    @Bind(R.id.myTextView)
    TextView myTextView;

    @Inject
    PreferenceService preferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.myButton)
    void onClickChangeText() {
        myTextView.setText(textAfter);
    }

    // TODO read/write preference

}
