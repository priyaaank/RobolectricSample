package com.pivotallabs;

public class TestCallbacks implements Callbacks {
    public boolean startWasCalled;
    public boolean succcessWasCalled;
    public boolean failureWasCalled;
    public boolean completeWasCalled;

    @Override
    public void onStart() {
        startWasCalled = true;
    }

    @Override
    public void onSuccess() {
        succcessWasCalled = true;
    }

    @Override
    public void onFailure() {
        failureWasCalled = true;
    }

    @Override
    public void onComplete() {
        completeWasCalled = true;
    }
}
