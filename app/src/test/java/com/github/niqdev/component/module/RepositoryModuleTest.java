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

    private boolean isMocked;

    public RepositoryModuleTest(boolean isMocked) {
        this.isMocked = isMocked;
    }

    @Provides
    @Singleton
    public MessageRepository provideMessageRepository() {
        return isMocked ? mock(MessageRepository.class) : new MessageRepositoryImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseHelper() {
        return mock(DatabaseRealm.class);
    }
}

