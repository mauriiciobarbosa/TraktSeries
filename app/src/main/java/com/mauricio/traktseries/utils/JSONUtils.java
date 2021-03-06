package com.mauricio.traktseries.utils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mauricio.traktseries.R;
import com.mauricio.traktseries.model.ObserverJSON;
import com.mauricio.traktseries.model.Serie;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * Classe utilitária responsável por disponibilizar comportamentos para trabalhar com JSON.
 *
 * Created by mauricio on 22/03/16.
 */
public class JSONUtils {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String API_VERSION = "trakt-api-version";
    public static final String API_KEY = "trakt-api-key";
    public static final String URL = "URL";

    private static Context applicationContext;
    private static RequestQueue mRequestQueue;

    private JSONUtils() {}

    public static void setApplicationContext(Context applicationContext) {
        JSONUtils.applicationContext = applicationContext;
    }

    private static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext);
        }
        return mRequestQueue;
    }


    /**
     * Método responsável por realizar requisição JSON. É necessário configurar o contexto da aplicação
     * antes de realizar requisição. Os parâmetros da requisição obrigatórios estão disponíveis através
     * de constantes.
     *
     *
     * @param observer - Objeto que será invocado após a resposta da API.
     * @param params - Parâmetros necessários para requisição.
     *
     */
    public static void makeRequest(final ObserverJSON observer, Map<String, String> params) {
        String url = params.get(URL);
        final String contentType = params.get(CONTENT_TYPE);
        final String apiKey = params.get(API_KEY);
        final String apiVersion = params.get(API_VERSION);

        if (applicationContext == null || url == null || contentType == null || apiKey == null ||
                apiVersion == null) {
            throw new IllegalStateException();
        }

        JsonArrayRequest request;
        request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        observer.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        observer.onError(R.string.error_load_series);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<>();
                headers.put(CONTENT_TYPE, contentType);
                headers.put(API_KEY, apiKey);
                headers.put(API_VERSION, apiVersion);
                return headers;
            }
        };

        getRequestQueue().add(request);
    }

    /**
     * Método responsável por converter um objeto JSONArray numa lista de séries.
     *
     * @param response - objeto JSONArray contendo informações sobre as séries mais populares.
     *
     * @return Lista de séries.
     */
    public static List<Serie> convertFromJSONArrayToObject(JSONArray response) {
        Gson converter = new Gson();
        Serie[] series = converter.fromJson(response.toString(), new TypeToken<Serie[]>(){}.getType());
        return Arrays.asList(series);
    }

}
