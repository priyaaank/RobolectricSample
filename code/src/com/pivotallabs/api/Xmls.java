package com.pivotallabs.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Xmls {
    public static Document getDocument(String xmlString) throws SAXException, IOException, ParserConfigurationException {
        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
    }

    public static String getChildElementBody(Element item, String elementName) {
        NodeList nodeList = item.getElementsByTagName(elementName);
        Element element = (Element) nodeList.item(0);
        return element.getChildNodes().item(0).getNodeValue();
    }
}
