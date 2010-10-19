package com.pivotallabs;

import android.net.Uri;
import com.xtremelabs.robolectric.AbstractRobolectricTestRunner;
import com.xtremelabs.robolectric.Loader;
import com.xtremelabs.robolectric.ProxyDelegatingHandler;
import org.junit.runners.model.InitializationError;

public class RobolectricTestRunner extends AbstractRobolectricTestRunner {
    private static final ProxyDelegatingHandler PROXY_DELEGATING_HANDLER = ProxyDelegatingHandler.getInstance();
    private static final Loader LOADER = new Loader(PROXY_DELEGATING_HANDLER);

    public RobolectricTestRunner(Class testClass) throws InitializationError {
        super(testClass, LOADER);
        setClassHandler(PROXY_DELEGATING_HANDLER);
        setTestHelperClass(TestHelper.class);
        
        LOADER.delegateLoadingOf(Uri.class.getName());
    }

    public static void addProxy(Class<?> realClass, Class<?> handlerClass) {
        PROXY_DELEGATING_HANDLER.addProxyClass(realClass, handlerClass);
    }

    @SuppressWarnings({"unchecked"})
    public static <P, R> P proxyFor(R instance) {
        return (P) PROXY_DELEGATING_HANDLER.proxyFor(instance);
    }
 }
