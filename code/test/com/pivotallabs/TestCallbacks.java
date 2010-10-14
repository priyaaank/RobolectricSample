package com.pivotallabs;

public class TestCallbacks implements Callbacks {
    public boolean succcessWasCalled;
    public boolean failuireWasCalled;
    public boolean completeWasCalled;

    @Override
    public void onSuccess() {
        succcessWasCalled = true;
    }

    @Override
    public void onFailure() {
        failuireWasCalled = true;
    }

    @Override
    public void onComplete() {
        completeWasCalled = true;
    }
}
