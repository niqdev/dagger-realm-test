package com.github.niqdev.repository.impl;

import com.github.niqdev.model.MessageModel;
import com.github.niqdev.repository.MessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class MessageRepositoryImpl implements MessageRepository {

    private static final Logger log = LoggerFactory.getLogger(MessageRepository.class);

    @Override
    public Observable<String> add(final String content, final String info) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    MessageModel model = MessageModel.newBuilder()
                        .content(content).info(info).build();

                    Realm realm = Realm.getDefaultInstance();
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
                    RealmResults<MessageModel> models =
                        Realm.getDefaultInstance().where(MessageModel.class).findAll();
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
