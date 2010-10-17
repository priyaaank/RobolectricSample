package com.pivotallabs.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        document.normalize();
        return document;
    }

    public static Element getElement(Document document, String tagName, int index) {
        return (Element) document.getElementsByTagName(tagName).item(index);
    }

    public static String getTextContentOfChild(Element element, String childTagName) {
        return getText(element.getElementsByTagName(childTagName));
    }

    public static String getTextContentOfChild(Document document, String childTagName) {
        return getText(document.getElementsByTagName(childTagName));
    }

    private static String getText(NodeList nodeList) {
        return nodeList.item(0).getFirstChild().getNodeValue();
    }
}
