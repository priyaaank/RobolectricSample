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

    public static ShadowZoomButtonsController shadowFor(ZoomButtonsController instance) {
        return (ShadowZoomButtonsController) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowGeoPoint shadowFor(GeoPoint instance) {
        return (ShadowGeoPoint) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowMapView shadowFor(MapView instance) {
        return (ShadowMapView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowMapController shadowFor(MapController instance) {
        return (ShadowMapController) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowItemizedOverlay shadowFor(ItemizedOverlay instance) {
        return (ShadowItemizedOverlay) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowListView shadowFor(ListView instance) {
        return (ShadowListView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowActivity shadowFor(Activity instance) {
        return (ShadowActivity) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowPaint shadowFor(Paint instance) {
        return (ShadowPaint) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowPath shadowFor(Path instance) {
        return (ShadowPath) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowHandler shadowFor(Handler instance) {
        return (ShadowHandler) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowIntent shadowFor(Intent instance) {
        return (ShadowIntent) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowView shadowFor(View instance) {
        return (ShadowView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowViewGroup shadowFor(ViewGroup instance) {
        return (ShadowViewGroup) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowAdapterView shadowFor(AdapterView instance) {
        return (ShadowAdapterView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowTextView shadowFor(TextView instance) {
        return (ShadowTextView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowImageView shadowFor(ImageView instance) {
        return (ShadowImageView) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowDialog shadowFor(Dialog instance) {
        return (ShadowDialog) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowAlertDialog shadowFor(AlertDialog instance) {
        return (ShadowAlertDialog) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowLooper shadowFor(Looper instance) {
        return (ShadowLooper) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowCanvas shadowFor(Canvas instance) {
        return (ShadowCanvas) RobolectricTestRunner.shadowFor(instance);
    }

    public static ShadowLocationManager shadowFor(LocationManager instance) {
        return (ShadowLocationManager) RobolectricTestRunner.shadowFor(instance);
    }

    public static void yieldToUiThread() {
        ShadowHandler.flush();
    }
}
