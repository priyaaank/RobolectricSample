package com.pivotallabs.injected;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;
import roboguice.inject.ContextScope;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override public void prepareTest(Object test) {
        InjectedApplication injectedApplication = (InjectedApplication) Robolectric.application;
        injectedApplication.setModule(new RobolectricSampleTestModule());
        Injector injector = injectedApplication.getInjector();
        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(injectedApplication);
        injector.injectMembers(test);
    }
}
