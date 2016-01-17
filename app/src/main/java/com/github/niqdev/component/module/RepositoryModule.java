package com.github.niqdev.component.module;

import com.github.niqdev.repository.DatabaseRealm;
import com.github.niqdev.repository.MessageRepository;
import com.github.niqdev.repository.impl.MessageRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public MessageRepository provideMessageRepository() {
        return new MessageRepositoryImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm();
    }
}
