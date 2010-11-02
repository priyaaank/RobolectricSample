package com.pivotallabs;

import android.net.Uri;
import com.xtremelabs.robolectric.AbstractRobolectricTestRunner;
import org.junit.runners.model.InitializationError;

public class RobolectricTestRunner extends AbstractRobolectricTestRunner {
    public RobolectricTestRunner(Class testClass) throws InitializationError {
        super(testClass, TestHelper.class);
        delegateLoadingOf(Uri.class.getName());
    }
 }
