package com.github.niqdev.component;

import com.github.niqdev.component.module.ApplicationContextModuleTest;
import com.github.niqdev.view.MainActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModuleTest.class})
public interface ApplicationComponentTest extends ApplicationComponent {

    void inject(MainActivityTest activity);

}
