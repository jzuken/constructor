package com.xcart.admin.managers;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Nikita on 29.09.2014.
 */
public class XCart5StatusConverter {

    private List<Pair<String, String>> statuses = new ArrayList<Pair<String, String>>();

    public XCart5StatusConverter(JSONArray statusesArray) {
        for (int i = 0; i < statusesArray.length(); i++) {
            try {
                JSONObject status = statusesArray.getJSONObject(i);
                statuses.add(new Pair<String, String>(status.getString("code"), status.getString("name")));
            } catch (JSONException e) {
            }
        }
    }

    public String getStatusBySymbol(String symbol) {
        for (Pair<String, String> status : statuses) {
            if (status.first.equals(symbol)) {
                return status.second;
            }
        }
        return null;
    }

    public String getSymbolByStatus(String statusName) {
        for (Pair<String, String> status : statuses) {
            if (status.second.equals(statusName)) {
                return status.first;
            }
        }
        return null;
    }
}
