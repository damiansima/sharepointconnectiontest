package com.mule.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mule.api.ConnectionException;
import org.mule.modules.sharepoint.SharepointConnector;
import org.mule.modules.sharepoint.microsoft.lists.GetListItems;
import org.mule.modules.sharepoint.microsoft.lists.GetListItemsResponse.GetListItemsResult;

public class SharePointTest {

    private String username;
    private String password;
    private String serviceurl;
    private SharepointConnector connector;

    public static void main(String[] args) throws Exception {
        SharePointTest test = new SharePointTest();

        String filePath;
        String rootPath;
        try {
            filePath = args[0];
            rootPath = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR - Missing parameter!!!");
            System.out.println("Usage: javax sharepointTest.jar [credentials.properties] [rootFolder]");
            System.out.println("");
            System.out
                    .println("* credentials.properties: path to the properties file with the connection credentials for the sharepoint instance.");
            System.out.println("* rootFolder: path to the sharepoint folder with out the first slash.");
            System.out.println("");
            System.out.println("Example: javax sharepointTest.jar credentials.properties dds1/folder");

            return;
        }

        System.out.println("Starting test");
        System.out.println("Reading properties from: " + filePath);
        test.initialization(filePath);

        System.out.println("Tring to obtain list of items for folder: " + rootPath);
        test.getListItems(rootPath);

    }

    public void initialization(String filePath) throws IOException, ConnectionException {
        InputStream stream = new FileInputStream(filePath);

        Properties prop = new Properties();
        prop.load(stream);

        // Save the props in the class attributes
        username = prop.getProperty("sp.username");
        password = prop.getProperty("sp.password");
        serviceurl = prop.getProperty("sp.serviceurl");

        System.out.println("Sharepoint username: " + username);
        System.out.println("Sharepoint password: " + password);
        System.err.println("Sharepoint url: " + serviceurl);

        connector = new SharepointConnector();
        connector.connect(username, password, serviceurl, null, null, null, null);

        stream.close();
    }

    public void getListItems(String rootPath) {
        CreateRootFolderRequest builder = new CreateRootFolderRequest();

        GetListItems request = builder.createRequest(rootPath);

        GetListItemsResult response = connector.listGetListItems(request);

    }

    // @Test
    // public void sipleCall2() {
    // GetListItems request = new GetListItems();
    // request.setListName("Shared Documents");
    //
    // GetListItemsResult response = connector.listGetListItems(request);
    // Assert.assertNotNull(response);
    // }
    //
    // @Test
    // public void simpleCallTest() {
    // GetUserCollectionFromSiteResult gucfsr =
    // connector.groupGetUserCollectionFromSite();
    // Assert.assertNotNull(gucfsr);
    // }
    //
    // @Test
    // public void testQuery() {
    // String qResult = connector.query(returnQueryString());
    // Assert.assertNotNull(qResult);
    // }
    //
    // private String returnQueryString() {
    // // Taken from msdn
    // // http://msdn.microsoft.com/en-us/library/ms473235(v=office.12).aspx
    // String QUERY_PACKET = "QueryPacket";
    // String QUERY = "Query";
    // // final String QUERY_ID = "QueryId";
    // String CONTEXT = "Context";
    // String QUERY_TEXT = "QueryText";
    // String QUERY_TEXT_ATTRIB_TYPE = "type";
    // String QUERY_TEXT_ATTRIB_LANGUAGE = "language";
    // String ORIG_CONTEXT = "OriginatorContext";
    //
    // // final String QUERY_TEXT_ATTRIB_TYPE_VALUE = "MSSQLFT";
    // // final String QUERY_TEXT_ATTRIB_LANGUAGE_VALUE = "xml:lang";
    // String QUERY_TEXT_ATTRIB_TYPE_VALUE = "STRING";
    // String QUERY_TEXT_ATTRIB_LANGUAGE_VALUE = "en-us";
    //
    // // String queryText = "SELECT * FROM Profile";
    // // WHERE lastUpdatedDate >
    // // " + message.getInvocationProperty("queryTimeStamp");
    // // String queryText =
    // //
    // "SELECT Title, Path, Description, Write, Rank, Size FROM Scope() WHERE FREETEXT(DEFAULTPROPERTIES,'SharePoint') ORDER BY Rank";
    //
    // String queryText = "Sharepoint";
    //
    // Document document = DocumentHelper.createDocument();
    // Element root = document.addElement(QUERY_PACKET);
    // Element query = root.addElement(QUERY);
    // Element context = query.addElement(CONTEXT);
    //
    // context.addElement(QUERY_TEXT).addAttribute(QUERY_TEXT_ATTRIB_TYPE,
    // QUERY_TEXT_ATTRIB_TYPE_VALUE)
    // .addAttribute(QUERY_TEXT_ATTRIB_LANGUAGE,
    // QUERY_TEXT_ATTRIB_LANGUAGE_VALUE).addText(queryText);
    //
    // context.addElement(ORIG_CONTEXT);
    //
    // return document.asXML();
    // }
    //
    // // @Test
    // public void testGetFileAsStream() throws FileNotFoundException,
    // IOException, URISyntaxException {
    // String url =
    // "http://ec2-54-234-255-54.compute-1.amazonaws.com/_vti_history/512/Shared Documents/FotoMarianoMerloUp.jpg";
    //
    // InputStream stream = connector.getFileAsStream(url.replace(" ", "%20"));
    //
    // Assert.assertNotNull(stream);
    //
    // }

}
