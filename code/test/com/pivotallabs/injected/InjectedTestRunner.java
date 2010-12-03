package com.pivotallabs.injected;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override public void prepareTest(Object test) {
        InjectedApplication injectedApplication = (InjectedApplication) Robolectric.application;
        injectedApplication.setModule(new RobolectricSampleTestModule());
        injectedApplication.getInjector().injectMembers(test);
    }
}
