package com.pivotallabs;

import android.net.Uri;
import com.xtremelabs.robolectric.AbstractRobolectricTestRunner;
import com.xtremelabs.robolectric.Loader;
import com.xtremelabs.robolectric.ShadowWrangler;
import org.junit.runners.model.InitializationError;

public class RobolectricTestRunner extends AbstractRobolectricTestRunner {
    private static final ShadowWrangler SHADOW_WRANGLER = ShadowWrangler.getInstance();
    private static final Loader LOADER = new Loader(SHADOW_WRANGLER);

    public RobolectricTestRunner(Class testClass) throws InitializationError {
        super(testClass, LOADER);
        setClassHandler(SHADOW_WRANGLER);
        setTestHelperClass(TestHelper.class);
        
        LOADER.delegateLoadingOf(Uri.class.getName());
    }
 }
