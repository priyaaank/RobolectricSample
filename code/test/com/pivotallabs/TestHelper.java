package com.pivotallabs;

import android.app.Activity;
import android.app.Application;
import com.pivotallabs.api.TestApiGateway;
import com.pivotallabs.tracker.AuthenticationGateway;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.res.ResourceLoader;
import com.xtremelabs.robolectric.shadows.ShadowApplication;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.util.TestHelperInterface;

import java.io.File;
import java.lang.reflect.Method;

@SuppressWarnings({"UnusedDeclaration"})
public class TestHelper implements TestHelperInterface {

    private static ResourceLoader resourceLoader;

    public static void signIn() {
        TestApiGateway apiGateway = new TestApiGateway();
        new AuthenticationGateway(apiGateway, new Activity())
                .signIn("spongebob", "squarepants", new Callbacks());
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
    }

    /**
     * This method is run before each test.  This is intended to be used as a global before each.
     */
    @Override
    public void before(Method method) {
        prepare();
        Robolectric.resetStaticState();
        Robolectric.application = ShadowApplication.bind(new Application(), resourceLoader);
    }

    /**
     * This method is run after  each test.  This is intended to be used as a global after each.
     */
    @Override
    public void after(Method method) {
    }


    @Override
    public void prepareTest(Object test) {
    }

    public static void prepare() {
        loadResources();

        Robolectric.bindDefaultShadowClasses();
    }


    public static void loadResources() {
       if (resourceLoader == null) {
           try {
               resourceLoader = new ResourceLoader(R.class, new File("res"));
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }
   }

    public static void yieldToUiThread() {
        ShadowHandler.flush();
    }
}
