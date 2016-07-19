package com.github.niqdev;

import android.app.Application;

import com.github.niqdev.component.Injector;
import com.github.niqdev.repository.DatabaseRealm;

import javax.inject.Inject;

public class CustomApplication extends Application {

    @Inject
    DatabaseRealm databaseRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
        // TODO
        // http://blog.sqisland.com/2015/12/mock-application-in-espresso.html
        //https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleRealmTest.java
        //https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleActivityTest.java
        //https://github.com/robolectric/robolectric/issues/1389
        //https://github.com/realm/realm-java/issues/904
        //databaseRealm.setup();
    }

}
