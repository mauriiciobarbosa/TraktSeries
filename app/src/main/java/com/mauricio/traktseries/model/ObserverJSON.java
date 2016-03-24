package com.mauricio.traktseries.model;

import org.json.JSONArray;

/**
 * Created by mauricio on 22/03/16.
 */
public interface ObserverJSON {

    /**
     * Método invocado quando é obtido uma resposta da requisição JSON.
     * @param response
     */
    public void onResponse(JSONArray response);

    /**
     * Método invocado quando ocorre um erro durante a requisição JSON.
     * @param idMessage - Identificador da mensagem no arquivo de strings.
     */
    public void onError(int idMessage);
}
