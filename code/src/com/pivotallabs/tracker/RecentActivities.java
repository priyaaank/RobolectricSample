package com.pivotallabs.tracker;

import com.pivotallabs.OnChangeListener;
import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class RecentActivities extends ArrayList<RecentActivity> {
    private ApiGateway apiGateway;
    private TrackerAuthenticator trackerAuthenticator;
    private OnChangeListener onChangeListener;

    public RecentActivities(ApiGateway apiGateway, TrackerAuthenticator trackerAuthenticator) {
        this.apiGateway = apiGateway;
        this.trackerAuthenticator = trackerAuthenticator;
    }

    public void update() {
        apiGateway.makeRequest(new RecentActivityRequest(trackerAuthenticator.getToken()), new RecentActivityApiResponseCallbacks());
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void changed() {
        if (onChangeListener != null) {
            onChangeListener.onChange();
        }
    }

    private class RecentActivityApiResponseCallbacks implements ApiResponseCallbacks {
        @Override
        public void onSuccess(ApiResponse response) {
            try {
                clear();
                NodeList activityNodeList = response.getResponseDocument().getElementsByTagName("activity");
                for (int i = 0; i < activityNodeList.getLength(); i++) {
                    add(new RecentActivity().apply((Element) activityNodeList.item(i)));
                }
                changed();
            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        @Override
        public void onFailure(ApiResponse response) {
            System.out.println("Failure retrieving recent activity: " + response.getResponseCode() + ":" + response.getResponseBody());
        }

        @Override
        public void onComplete() {
        }
    }
}
