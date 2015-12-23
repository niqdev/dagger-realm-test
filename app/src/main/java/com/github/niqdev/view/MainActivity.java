package com.github.niqdev.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
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

    @Bind(R.id.textViewExample1)
    TextView textViewExample1;

    @Bind(R.id.editTextExample2)
    EditText editTextExample2;

    //@Inject
    PreferenceService preferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        //initPreference();
    }

    @OnClick(R.id.buttonExample1)
    void onClickChangeText() {
        textViewExample1.setText(textAfter);
    }

    private void initPreference() {
        editTextExample2.setText(preferenceService.readMyPreference());
    }

    @OnClick(R.id.buttonExample2)
    void onClickEditPreference() {
        String value = editTextExample2.getText().toString();
        if (!TextUtils.isEmpty(value)) {
            preferenceService.saveMyPreference(value);
        }
    }

}
