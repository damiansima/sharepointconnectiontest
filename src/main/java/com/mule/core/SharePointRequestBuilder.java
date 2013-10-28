package com.mule.core;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SharePointRequestBuilder {

    /**
     * It Builds a XML SP request, that will look for a definitive folder path
     * 
     * @param rootFolderPath
     *            the path of the root folder
     * @return the XML request
     */
    public static String buildQueryForRootFolder(String rootFolderPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("<Query><Where>");
        sb.append("<And>");
        sb.append("<Eq>");
        sb.append("<FieldRef Name='FileRef'/>");
        sb.append("<Value Type='Lookup'>" + rootFolderPath + "</Value>");
        sb.append("</Eq>");
        sb.append("<Eq>");
        sb.append("<FieldRef Name='FSObjType'/>");
        sb.append("<Value Type='Lookup'>1</Value>");
        sb.append("</Eq>");
        sb.append("</And>");
        sb.append("</Where>");
        sb.append("</Query>");

        return sb.toString();
    }

    /**
     * It Builds a XML SP request, that will retrieve the complete folder
     * structure from a root folder path.
     * 
     * @param rootFolderPath
     *            the path of the root folder
     * @return the XML request
     */
    public static String buildQueryOptionsForFolders(String rootFolderPath, String nextPageToken) {
        StringBuilder sb = new StringBuilder();

        sb.append("<QueryOptions>");
        sb.append("<IncludeMandatoryColumns>FALSE</IncludeMandatoryColumns>");
        sb.append("<ViewAttributes Scope='RecursiveAll' />");
        sb.append("<DateInUtc>TRUE</DateInUtc>");

        if (!StringUtils.isEmpty(rootFolderPath)) {
            sb.append("<Folder>" + stripTrailingSlash(rootFolderPath) + "</Folder>");
            // sb.append("<Folder>/" +
            // stripLeadingAndTrailingSlashes(rootFolderPath) + "</Folder>");
        }

        if (!StringUtils.isEmpty(nextPageToken))
            sb.append("<Paging ListItemCollectionPositionNext='" + nextPageToken.replace("&", "&amp;") + "'/>");

        sb.append("</QueryOptions>");

        return sb.toString();
    }

    /**
     * It generats a {@link Node} that can be added to a SharePoint POJO as
     * content to be serialized and send
     * 
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Node buildXmlContentNode(String xml) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documentOptions = builder.parse(new InputSource(new StringReader(xml)));
        Node elementOptions = documentOptions.getDocumentElement();

        return elementOptions;
    }

    private static String stripLeadingAndTrailingSlashes(String path) {
        path = stripLeadingSlash(path);
        path = stripTrailingSlash(path);

        return path;
    }

    private static String stripLeadingSlash(String path) {
        return StringUtils.stripStart(path, "/");
    }

    private static String stripTrailingSlash(String path) {
        return StringUtils.stripEnd(path, "/");
    }

}
