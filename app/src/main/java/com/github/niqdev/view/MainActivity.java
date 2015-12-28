package com.github.niqdev.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.niqdev.R;
import com.github.niqdev.component.Injector;
import com.github.niqdev.model.MessageModel;
import com.github.niqdev.repository.MessageRepository;
import com.github.niqdev.service.PreferenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);

    @BindString(R.string.text_after)
    String textAfter;

    @BindString(R.string.validation_required)
    String validationRequired;

    @Bind(R.id.textViewExample1)
    TextView textViewExample1;

    @Bind(R.id.editTextExample2)
    EditText editTextExample2;

    @Bind(R.id.editTextExample3Content)
    EditText editTextExample3Content;

    @Bind(R.id.editTextExample3Info)
    EditText editTextExample3Info;

    @Bind(R.id.listViewExample3)
    ListView listViewExample3;

    @Inject
    PreferenceService preferenceService;

    @Inject
    MessageRepository messageRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        initPreference();
        refreshMessages();
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
            preferenceService.writeMyPreference(value);
        }
    }

    // use lambda and stream!
    private void refreshMessages() {
        messageRepository.findAll()
            .subscribe(new Action1<List<MessageModel>>() {
                @Override
                public void call(List<MessageModel> messages) {
                    List<String> values = new ArrayList<>();
                    for (MessageModel message : messages) {
                        values.add(message.getContent());
                    }
                    listViewExample3.setAdapter(
                        new ArrayAdapter<>(MainActivity.this.getBaseContext(), android.R.layout.simple_list_item_1, values));
                }
            });
    }

    @OnClick(R.id.buttonExample3)
    void onClickAddMessage() {
        String content = editTextExample3Content.getText().toString();
        String info = editTextExample3Info.getText().toString();

        editTextExample3Content.setError(null);
        if (TextUtils.isEmpty(content)) {
            editTextExample3Content.setError(validationRequired);
        } else {
            MessageModel message = MessageModel.newBuilder().content(content).info(info).build();
            editTextExample3Content.setText("");
            editTextExample3Info.setText("");

            messageRepository.add(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        log.debug("onClickAddMessage UUID={}", s);
                        ((BaseAdapter) listViewExample3.getAdapter()).notifyDataSetChanged();
                        refreshMessages();
                    }
                });
        }
    }

}
