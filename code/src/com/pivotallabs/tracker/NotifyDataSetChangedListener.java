package com.pivotallabs.tracker;

import android.widget.BaseAdapter;
import com.pivotallabs.OnChangeListener;

public class NotifyDataSetChangedListener implements OnChangeListener {
    private final BaseAdapter baseAdapter;

    public NotifyDataSetChangedListener(BaseAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onChange() {
        baseAdapter.notifyDataSetChanged();
    }
}
