package com.github.niqdev.component.module;

import com.github.niqdev.repository.MessageRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class RepositoryModuleTest {

    @Provides
    @Singleton
    public MessageRepository provideMessageRepository() {
        return mock(MessageRepository.class);
    }
}

