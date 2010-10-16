package com.pivotallabs.tracker;

import com.pivotallabs.Callbacks;
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

    public void update(Callbacks callbacks) {
        callbacks.onStart();
        apiGateway.makeRequest(new RecentActivityRequest(trackerAuthenticator.getToken()),
                new RecentActivityApiResponseCallbacks(callbacks));
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
        private Callbacks callbacks;

        public RecentActivityApiResponseCallbacks(Callbacks callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onSuccess(ApiResponse response) {
            try {
                clear();
                NodeList activityNodeList = response.getResponseDocument().getElementsByTagName("activity");
                for (int i = 0; i < activityNodeList.getLength(); i++) {
                    add(new RecentActivity().apply((Element) activityNodeList.item(i)));
                }
                changed();
                callbacks.onSuccess();
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
            callbacks.onFailure();
        }

        @Override
        public void onComplete() {
            callbacks.onComplete();
        }
    }
}
