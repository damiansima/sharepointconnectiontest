package com.mule.core;

import static com.mule.core.SharePointRequestBuilder.buildQueryForRootFolder;
import static com.mule.core.SharePointRequestBuilder.buildQueryOptionsForFolders;
import static com.mule.core.SharePointRequestBuilder.buildXmlContentNode;

import org.apache.log4j.Logger;
import org.mule.modules.sharepoint.microsoft.lists.GetListItems;
import org.mule.modules.sharepoint.microsoft.lists.GetListItems.Query;
import org.mule.modules.sharepoint.microsoft.lists.GetListItems.QueryOptions;
import org.w3c.dom.Node;

public class CreateRootFolderRequest {
    private final static Logger logger = Logger.getLogger(CreateRootFolderRequest.class);
    public static final String PATH_SEPARATOR = "/";

    public GetListItems createRequest(String rootPath) {

        String listName = rootPath.split(PATH_SEPARATOR)[0];

        logger.debug("Creating SharePoint request to query folder for: " + rootPath + " ...");

        GetListItems request = new GetListItems();

        request.setListName(listName);

        if (!rootPath.equals(listName)) {
            Node xmlQuery = buildQuery(rootPath);
            Query query = new Query();
            query.getContent().add(xmlQuery);
            request.setQuery(query);
        }

        Node xmlQueryOptions = buildQueryOptions();
        QueryOptions options = new QueryOptions();
        options.getContent().add(xmlQueryOptions);
        request.setQueryOptions(options);

        return request;
    }

    private Node buildQuery(String rootPath) {
        Node xmlQuery = null;
        try {
            String queryForRootFolder = buildQueryForRootFolder(rootPath);
            logger.debug("Query for root folder: " + queryForRootFolder);

            xmlQuery = buildXmlContentNode(queryForRootFolder);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating the SharePoint Request: Query Options.", e);
        }
        return xmlQuery;
    }

    private Node buildQueryOptions() {
        Node xmlQueryOptions = null;
        try {
            String queryOptionsForFolders = buildQueryOptionsForFolders(null, null);
            logger.debug("Query Options for folder: " + queryOptionsForFolders);

            xmlQueryOptions = buildXmlContentNode(queryOptionsForFolders);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating the SharePoint Request: Query Options.", e);
        }
        return xmlQueryOptions;
    }

}
