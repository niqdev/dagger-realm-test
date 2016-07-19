package com.github.niqdev;

import com.github.niqdev.component.Injector;

/**
 * @author niqdev
 */
public class CustomApplicationTest extends CustomApplication {

    /**
     * <code>databaseRealm.setup()</code> throws java.lang.UnsatisfiedLinkError for testing
     */
    @Override
    protected void initApplication() {
        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
    }

}
