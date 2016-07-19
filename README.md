#Dagger 2 and Realm unit test

[![Build Status](https://travis-ci.org/niqdev/dagger-realm-test.svg?branch=master)](https://travis-ci.org/niqdev/dagger-realm-test)
[![Coverage Status](https://coveralls.io/repos/github/niqdev/dagger-realm-test/badge.svg?branch=master)](https://coveralls.io/github/niqdev/dagger-realm-test?branch=master)

:tada: Updated with Realm v1.x!! <-- Robolectric/Realm [issue](https://github.com/robolectric/robolectric/issues/1389)

Sample Android application of [Dagger 2](http://google.github.io/dagger/) and [Realm](https://realm.io/docs/java/latest/) tested with [Robolectric](http://robolectric.org/), [Mockito](http://mockito.org/) and [PowerMockito](https://github.com/jayway/powermock).

### Example
```java
@RunWith(RobolectricGradleTestRunner.class)
@Config(application = CustomApplicationTest.class, constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest({Injector.class})
public class MessageRepositoryTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Inject
    MessageRepository messageRepository;

    @Inject
    DatabaseRealm databaseRealm;

    @Before
    public void setupDagger() {
        ApplicationComponentTest applicationComponentTest = DaggerApplicationComponentTest.builder()
            .applicationContextModuleTest(new ApplicationContextModuleTest())
            .repositoryModuleTest(new RepositoryModuleTest(false))
            .build();

        PowerMockito.mockStatic(Injector.class);
        PowerMockito.when(Injector.getApplicationComponent()).thenReturn(applicationComponentTest);

        ((ApplicationComponentTest) Injector.getApplicationComponent()).inject(this);
    }

    @Test
    public void messageRepository_add() {
        String MESSAGE_UUID = "UUID";
        String MESSAGE_CONTENT = "CONTENT";
        String MESSAGE_INFO = "INFO";
        MessageModel message = new MessageModel();
        message.setUuid(MESSAGE_UUID);
        message.setContent(MESSAGE_CONTENT);
        message.setInfo(MESSAGE_INFO);

        when(databaseRealm.add(message)).thenReturn(message);

        TestSubscriber<String> tester = new TestSubscriber<>();
        messageRepository.add(message).subscribe(tester);

        verify(databaseRealm).add(message);

        tester.assertValue(MESSAGE_UUID);
        tester.assertCompleted();
        tester.assertNoErrors();
    }

    @Test
    public void messageRepository_findAll() {
        MessageModel message1 = MessageModel.newBuilder().content("CONTENT1").build();
        MessageModel message2 = MessageModel.newBuilder().content("CONTENT2").build();
        List<MessageModel> messages = Arrays.asList(message1, message2);

        when(databaseRealm.findAll(MessageModel.class)).thenReturn(messages);

        TestSubscriber<List<MessageModel>> tester = new TestSubscriber<>();
        messageRepository.findAll().subscribe(tester);

        tester.assertValue(messages);
        tester.assertCompleted();
        tester.assertNoErrors();
    }

}
```

Setup [Robolectric](http://robolectric.org/getting-started/)

- select test directory
- Build > Select Build Variant
- Test Artifact: Unit Tests

for Linux and Mac users:

- Edit Configurations > Working directory = $MODULE_DIR$

<!--
Links/issues

- http://stackoverflow.com/questions/27036933/how-to-set-up-dagger-dependency-injection-from-scratch-in-android-project
- https://stackoverflow.com/questions/26939340/how-do-you-override-a-module-dependency-in-a-unit-test-with-dagger-2-0/29996385#29996385
- https://github.com/robolectric/robolectric/issues/1389
- https://github.com/google/dagger/issues/186#issuecomment-163309550
- https://github.com/google/dagger/issues/110
- https://stackoverflow.com/questions/29969913/creating-test-dependencies-when-using-dagger2
- http://stackoverflow.com/questions/29989245/android-unit-tests-with-dagger-2
- https://stackoverflow.com/questions/26939340/how-do-you-override-a-module-dependency-in-a-unit-test-with-dagger-2-0?lq=1
- https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleRealmTest.java
- https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleActivityTest.java
- https://github.com/robolectric/robolectric/issues/1389
- https://github.com/realm/realm-java/issues/904
- http://blog.sqisland.com/2015/12/mock-application-in-espresso.html
- https://artemzin.com/blog/how-to-mock-dependencies-in-unit-integration-and-functional-tests-dagger-robolectric-instrumentation/

-->
