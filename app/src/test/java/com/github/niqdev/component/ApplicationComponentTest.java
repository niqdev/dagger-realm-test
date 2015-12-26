package com.github.niqdev.component;

import com.github.niqdev.component.module.ApplicationContextModuleTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModuleTest.class})
public interface ApplicationComponentTest extends ApplicationComponent {

}
