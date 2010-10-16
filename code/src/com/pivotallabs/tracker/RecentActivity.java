package com.pivotallabs.tracker;

import com.pivotallabs.api.Xmls;
import org.w3c.dom.Element;

public class RecentActivity {

    private String description;

    public RecentActivity applyXmlElement(Element item) {
        this.description = Xmls.getChildElementBody(item, "description");
        return this;
    }

    public RecentActivity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }
}
