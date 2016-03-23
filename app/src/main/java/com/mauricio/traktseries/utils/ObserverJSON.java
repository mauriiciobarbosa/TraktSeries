package com.mauricio.traktseries.utils;

import org.json.JSONArray;

/**
 * Created by mauricio on 22/03/16.
 */
public interface ObserverJSON {

    public void onResponse(JSONArray response);

    public void onError(String message);
}
