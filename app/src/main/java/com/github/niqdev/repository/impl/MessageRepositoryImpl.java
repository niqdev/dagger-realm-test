package com.github.niqdev.repository.impl;

import com.github.niqdev.component.Injector;
import com.github.niqdev.model.MessageModel;
import com.github.niqdev.repository.DatabaseHelper;
import com.github.niqdev.repository.MessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class MessageRepositoryImpl implements MessageRepository {

    private static final Logger log = LoggerFactory.getLogger(MessageRepository.class);

    @Inject
    DatabaseHelper databaseHelper;

    public MessageRepositoryImpl() {
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Realm realm = databaseHelper.getRealmInstance();
                    realm.beginTransaction();
                    realm.copyToRealm(model);
                    realm.commitTransaction();

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    log.error("ADD", e);
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    Realm realm = databaseHelper.getRealmInstance();

                    RealmResults<MessageModel> models = realm.where(MessageModel.class).findAll();
                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    log.error("FIND_ALL", e);
                    subscriber.onError(e);
                }
            }
        });
    }
}
