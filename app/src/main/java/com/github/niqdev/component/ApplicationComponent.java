package com.github.niqdev.component;

import com.github.niqdev.component.module.ApplicationContextModule;
import com.github.niqdev.component.module.RepositoryModule;
import com.github.niqdev.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);

}
