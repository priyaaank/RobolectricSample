package com.pivotallabs;

import android.view.View;

public class ViewVisibleWhileOutstandingCallbacks implements Callbacks {
    private View view;

    public ViewVisibleWhileOutstandingCallbacks(View view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure() {
    }

    @Override
    public void onComplete() {
        view.setVisibility(View.GONE);
    }
}
