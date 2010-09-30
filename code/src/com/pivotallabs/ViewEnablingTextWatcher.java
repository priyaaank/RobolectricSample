package com.pivotallabs;

import android.text.Editable;
import android.view.View;
import android.widget.TextView;

class ViewEnablingTextWatcher extends StubTextWatcher {


    public ViewEnablingTextWatcher(View toEnable, TextView...  toWatch) {
        super();
        
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
