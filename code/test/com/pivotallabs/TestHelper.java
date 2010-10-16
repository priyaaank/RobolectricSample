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
import com.xtremelabs.robolectric.fakes.*;
import com.xtremelabs.robolectric.res.ResourceLoader;
import com.xtremelabs.robolectric.util.FakeHelper;
import com.xtremelabs.robolectric.util.Implements;
import com.xtremelabs.robolectric.util.TestHelperInterface;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class TestHelper implements TestHelperInterface {

    private static ResourceLoader resourceLoader;

    /**
     * This method is run before each test.  This is intended to be used as a global before each.
     */
    @Override
    public void before(Method method) {
        prepare();
        FakeHelper.resetRobolectricTestState();
        FakeHelper.application = FakeApplication.bind(new Application(), resourceLoader);
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
        List<Class<?>> genericProxies = FakeHelper.getGenericProxies();
        for (Class<?> genericProxy : genericProxies) {
            Implements implementsClass = genericProxy.getAnnotation(Implements.class);
            FastAndroidTestRunner.addProxy(implementsClass.value(), genericProxy);
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

    public static FakeZoomButtonsController proxyFor(ZoomButtonsController instance) {
        return (FakeZoomButtonsController) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeGeoPoint proxyFor(GeoPoint instance) {
        return (FakeGeoPoint) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeMapView proxyFor(MapView instance) {
        return (FakeMapView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeMapController proxyFor(MapController instance) {
        return (FakeMapController) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeItemizedOverlay proxyFor(ItemizedOverlay instance) {
        return (FakeItemizedOverlay) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeListView proxyFor(ListView instance) {
        return (FakeListView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeActivity proxyFor(Activity instance) {
        return (FakeActivity) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakePaint proxyFor(Paint instance) {
        return (FakePaint) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakePath proxyFor(Path instance) {
        return (FakePath) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeHandler proxyFor(Handler instance) {
        return (FakeHandler) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeIntent proxyFor(Intent instance) {
        return (FakeIntent) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeView proxyFor(View instance) {
        return (FakeView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeViewGroup proxyFor(ViewGroup instance) {
        return (FakeViewGroup) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeAdapterView proxyFor(AdapterView instance) {
        return (FakeAdapterView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeTextView proxyFor(TextView instance) {
        return (FakeTextView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeImageView proxyFor(ImageView instance) {
        return (FakeImageView) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeDialog proxyFor(Dialog instance) {
        return (FakeDialog) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeAlertDialog proxyFor(AlertDialog instance) {
        return (FakeAlertDialog) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeLooper proxyFor(Looper instance) {
        return (FakeLooper) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeCanvas proxyFor(Canvas instance) {
        return (FakeCanvas) FastAndroidTestRunner.proxyFor(instance);
    }

    public static FakeLocationManager proxyFor(LocationManager instance) {
        return (FakeLocationManager) FastAndroidTestRunner.proxyFor(instance);
    }

    public static void yieldToUiThread() {
        FakeHandler.flush();
    }
}
