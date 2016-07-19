package com.github.niqdev.component;

import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.component.module.RepositoryModuleTest;
import com.github.niqdev.repository.DatabaseRealmTest;
import com.github.niqdev.repository.MessageRepositoryTest;
import com.github.niqdev.view.MainActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModuleTest.class, RepositoryModuleTest.class})
public interface ApplicationComponentTest extends ApplicationComponent {

    void inject(MainActivityTest activity);
    void inject(MessageRepositoryTest repository);
    void inject(DatabaseRealmTest database);

}
