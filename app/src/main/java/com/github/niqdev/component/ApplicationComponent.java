package com.github.niqdev.component;

import com.github.niqdev.CustomApplication;
import com.github.niqdev.component.module.ApplicationContextModule;
import com.github.niqdev.component.module.RepositoryModule;
import com.github.niqdev.repository.DatabaseHelper;
import com.github.niqdev.repository.impl.MessageRepositoryImpl;
import com.github.niqdev.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CustomApplication application);
    void inject(MainActivity mainActivity);

    void inject(DatabaseHelper databaseHelper);
    void inject(MessageRepositoryImpl messageRepository);

}
