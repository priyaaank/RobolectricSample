package com.pivotallabs;

import android.net.Uri;
import com.xtremelabs.droidsugar.AbstractAndroidTestRunner;
import com.xtremelabs.droidsugar.Loader;
import com.xtremelabs.droidsugar.ProxyDelegatingHandler;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;

public class FastAndroidTestRunner extends AbstractAndroidTestRunner {
    private static final ProxyDelegatingHandler PROXY_DELEGATING_HANDLER = ProxyDelegatingHandler.getInstance();
    private static final Loader LOADER = new Loader(PROXY_DELEGATING_HANDLER);
    private TestHelperInterface testHelper;

    public FastAndroidTestRunner(Class testClass) throws InitializationError {
        super(testClass, LOADER, PROXY_DELEGATING_HANDLER);
        LOADER.delegateLoadingOf(TestHelperInterface.class.getName());
        LOADER.delegateLoadingOf(Uri.class.getName());
    }

    public static void addProxy(Class<?> realClass, Class<?> handlerClass) {
        PROXY_DELEGATING_HANDLER.addProxyClass(realClass, handlerClass);
    }

    @SuppressWarnings({"unchecked"})
    public static <P, R> P proxyFor(R instance) {
        return (P) PROXY_DELEGATING_HANDLER.proxyFor(instance);
    }

    @Override
    protected void beforeTest(Method method) {
        testHelper = createTestHelper(method);
        testHelper.before(method);

        super.beforeTest(method);
    }

    @Override
    protected void afterTest(Method method) {
        super.afterTest(method);

        testHelper.after(method);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        testHelper.prepareTest(test);
        return test;
    }

    private TestHelperInterface createTestHelper(Method method) {
        Class<?> testClass = method.getDeclaringClass();
        try {
            return (TestHelperInterface) testClass.getClassLoader().loadClass(TestHelper.class.getName()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
