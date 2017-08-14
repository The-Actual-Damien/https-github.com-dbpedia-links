package org.dbpedia.links.lib;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

//todo implement patches
public class LinkSet {

    String uri;

    String endpoint = null;
    String script = null;
    int updateFrequencyInDays = 0;
    String outputFile = null;

    List<String> ntriplefilelocations = new ArrayList<String>();
    List<String> linkConfs = new ArrayList<String>();
    List<String> constructqueries = new ArrayList<String>();
    List<Issue> issues = new ArrayList<Issue>();


    public LinkSet(String uri) {
        this.uri = uri;

    }



    @Override
    public String toString() {
        return "LinkSet{" +
                "\nuri='" + uri + '\'' +
                "\n, ntriplefilelocations=" + ntriplefilelocations +
                "\n, linkConfs=" + linkConfs +
                "\n, endpoint='" + endpoint + '\'' +
                "\n, constructqueries=" + constructqueries +
                "\n, script='" + script + '\'' +
                "\n, updateFrequencyInDays='" + updateFrequencyInDays + '\'' +
                "\n, outputFile='" + outputFile + '\'' +
                '}';
    }
}