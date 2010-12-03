package com.pivotallabs.injected;

import com.google.inject.Scopes;
import roboguice.config.AbstractAndroidModule;

public class RobolectricSampleTestModule extends AbstractAndroidModule {

    @Override protected void configure() {
       bind(Counter.class).in(Scopes.SINGLETON);
    }
}
