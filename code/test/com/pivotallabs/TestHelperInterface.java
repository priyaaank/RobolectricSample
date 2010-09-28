package com.pivotallabs;

import java.lang.reflect.Method;

public interface TestHelperInterface {
    void before(Method method);

    void after(Method method);

    void prepareTest(Object test);
}
