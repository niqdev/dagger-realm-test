package com.github.niqdev;

/**
 * @author niqdev
 */
public class CustomApplicationTest extends CustomApplication {

    @Override
    protected void initRealm() {
        // do nothing: databaseRealm.setup() throws java.lang.UnsatisfiedLinkError for testing
    }

}
