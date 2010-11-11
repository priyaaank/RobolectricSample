package com.pivotallabs;

import android.net.Uri;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

public class RobolectricSampleTestRunner extends RobolectricTestRunner {
    public RobolectricSampleTestRunner(Class testClass) throws InitializationError {
        super(testClass);
        delegateLoadingOf(Uri.class.getName());
    }
 }
