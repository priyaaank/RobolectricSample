package com.pivotallabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.pivotallabs.api.TestApiGateway;
import com.pivotallabs.tracker.AuthenticationGateway;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.fakes.*;
import com.xtremelabs.robolectric.res.ResourceLoader;
import com.xtremelabs.robolectric.util.Implements;
import com.xtremelabs.robolectric.util.TestHelperInterface;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

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
        setProxies();
    }

    public static void setProxies() {
        List<Class<?>> genericProxies = Robolectric.getGenericProxies();
        for (Class<?> genericProxy : genericProxies) {
            Implements implementsClass = genericProxy.getAnnotation(Implements.class);
            RobolectricTestRunner.addProxy(implementsClass.value(), genericProxy);
        }
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

    public static ShadowZoomButtonsController proxyFor(ZoomButtonsController instance) {
        return (ShadowZoomButtonsController) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowGeoPoint proxyFor(GeoPoint instance) {
        return (ShadowGeoPoint) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowMapView proxyFor(MapView instance) {
        return (ShadowMapView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowMapController proxyFor(MapController instance) {
        return (ShadowMapController) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowItemizedOverlay proxyFor(ItemizedOverlay instance) {
        return (ShadowItemizedOverlay) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowListView proxyFor(ListView instance) {
        return (ShadowListView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowActivity proxyFor(Activity instance) {
        return (ShadowActivity) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowPaint proxyFor(Paint instance) {
        return (ShadowPaint) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowPath proxyFor(Path instance) {
        return (ShadowPath) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowHandler proxyFor(Handler instance) {
        return (ShadowHandler) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowIntent proxyFor(Intent instance) {
        return (ShadowIntent) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowView proxyFor(View instance) {
        return (ShadowView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowViewGroup proxyFor(ViewGroup instance) {
        return (ShadowViewGroup) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowAdapterView proxyFor(AdapterView instance) {
        return (ShadowAdapterView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowTextView proxyFor(TextView instance) {
        return (ShadowTextView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowImageView proxyFor(ImageView instance) {
        return (ShadowImageView) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowDialog proxyFor(Dialog instance) {
        return (ShadowDialog) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowAlertDialog proxyFor(AlertDialog instance) {
        return (ShadowAlertDialog) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowLooper proxyFor(Looper instance) {
        return (ShadowLooper) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowCanvas proxyFor(Canvas instance) {
        return (ShadowCanvas) RobolectricTestRunner.proxyFor(instance);
    }

    public static ShadowLocationManager proxyFor(LocationManager instance) {
        return (ShadowLocationManager) RobolectricTestRunner.proxyFor(instance);
    }

    public static void yieldToUiThread() {
        ShadowHandler.flush();
    }
}
