package com.pivotallabs.tracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.pivotallabs.NotifyDataSetChangedCallbacks;
import com.pivotallabs.R;
import com.pivotallabs.ViewVisibleWhileOutstandingCallbacks;
import com.pivotallabs.api.ApiGateway;

public class RecentActivityActivity extends Activity {

    ApiGateway apiGateway = new ApiGateway();
    SignInDialog signInDialog;

    private View loadingView;
    private TrackerAuthenticator trackerAuthenticator;
    private RecentActivities recentActivities;
    private ViewVisibleWhileOutstandingCallbacks showLoadingWhileOutstanding;
    private NotifyDataSetChangedCallbacks notifyDataSetChangedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_activity_layout);

        trackerAuthenticator = new TrackerAuthenticator(apiGateway, this);
        recentActivities = new RecentActivities(apiGateway, trackerAuthenticator);

        ListView recentActivityListView = (ListView) findViewById(R.id.recent_activity_list);

        loadingView = getLayoutInflater().inflate(R.layout.loading_view, recentActivityListView, false);
        recentActivityListView.addFooterView(loadingView);
        recentActivityListView.setFooterDividersEnabled(false);

        RecentActivityAdapter recentActivityAdapter = new RecentActivityAdapter(recentActivities, getLayoutInflater());
        recentActivityListView.setAdapter(recentActivityAdapter);

        showLoadingWhileOutstanding = new ViewVisibleWhileOutstandingCallbacks(loadingView);
        notifyDataSetChangedCallbacks = new NotifyDataSetChangedCallbacks(recentActivityAdapter);

        if (!trackerAuthenticator.isAuthenticated()) {
            showSignInDialog();
        } else {
            update();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        MenuItem signOutMenuItem = menu.add("Sign Out");
        signOutMenuItem.setEnabled(trackerAuthenticator.isAuthenticated());
        signOutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                trackerAuthenticator.signOut();
                finish();
                return true;
            }
        });
        return true;
    }

    private void update() {
        recentActivities.update(
                showLoadingWhileOutstanding,
                notifyDataSetChangedCallbacks
        );
    }

    private void showSignInDialog() {
        signInDialog = new SignInDialog(this, trackerAuthenticator);
        signInDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (trackerAuthenticator.isAuthenticated()) {
                    update();
                } else {
                    finish();
                }
            }
        });
        signInDialog.show();
    }
}
