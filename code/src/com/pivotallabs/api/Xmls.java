package com.pivotallabs.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    public static String getTextContentOfChild(Element item, String childTagName) {
        return item.getElementsByTagName(childTagName).item(0).getTextContent();
    }
}
