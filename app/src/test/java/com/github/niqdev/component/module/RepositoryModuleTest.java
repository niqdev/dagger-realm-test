package com.github.niqdev.component.module;

import com.github.niqdev.repository.DatabaseRealm;
import com.github.niqdev.repository.MessageRepository;
import com.github.niqdev.repository.impl.MessageRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class RepositoryModuleTest {

    private boolean isMockedRepository;
    private boolean isMockedDatabase;

    // for simplicity use variables, but you should create separated modules if needed
    public RepositoryModuleTest(boolean mockRepository, boolean mockDatabase) {
        this.isMockedRepository = mockRepository;
        this.isMockedDatabase = mockDatabase;
    }

    @Provides
    @Singleton
    public MessageRepository provideMessageRepository() {
        return isMockedRepository ? mock(MessageRepository.class) : new MessageRepositoryImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseHelper() {
        return isMockedDatabase ? mock(DatabaseRealm.class) : new DatabaseRealm();
    }
}

