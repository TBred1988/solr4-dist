package com.tryapp.solr.client;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Tibor Kovács on 2015.01.31..
 */
public class Client {

    public static void main(String[] args) {
        SolrServer server = new HttpSolrServer("http://localhost:8083/solr");
        QueryResponse response = getAllDocuments(server);
        if (response.getResults().size() < 10) {
            createRandomDocument(server);
            response = getAllDocuments(server);
        }

        for (SolrDocument document : response.getResults()) {
            System.out.println(document.values());
        }
    }

    private static void createRandomDocument(SolrServer server) {
        Random random = new Random();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "id-" + Math.abs(random.nextInt() / 1000), 1.0f);
        doc.addField("name", "doc-" + Math.abs(random.nextInt() / 1000), 1.0f);
        doc.addField("price", 20 + Math.abs(random.nextInt() / 1000));
        try {
            server.add(doc);
            server.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static QueryResponse getAllDocuments(SolrServer server) {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.setFacet(true);
        query.set("wt", "json");
        query.setRows(Integer.MAX_VALUE);
        query.setStart(0);

        QueryResponse response = null;
        try {
            response = server.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return response;
    }
}
