package com.github.niqdev.repository;

import com.github.niqdev.model.MessageModel;

import java.util.List;

import rx.Observable;

public interface MessageRepository {

    Observable<String> add(String content, String info);

    Observable<List<MessageModel>> findAll();

}
