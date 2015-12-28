package com.github.niqdev.component;

import com.github.niqdev.CustomApplication;
import com.github.niqdev.component.module.ApplicationContextModule;
import com.github.niqdev.component.module.RepositoryModule;

import java.util.Objects;

public class Injector {

    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(CustomApplication customApplication) {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationContextModule(new ApplicationContextModule(customApplication))
            .repositoryModule(new RepositoryModule())
            .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}
