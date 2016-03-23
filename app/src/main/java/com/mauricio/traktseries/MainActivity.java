package com.mauricio.traktseries;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Action;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mauricio.traktseries.model.Serie;
import com.mauricio.traktseries.model.SerieAdapter;
import com.mauricio.traktseries.utils.JSONUtils;
import com.mauricio.traktseries.utils.ObserverJSON;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements ObserverJSON {

    @ViewById(R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewById(R.id.progress)
    public ProgressBar spinner;

    private List<Serie> series;
    private SerieAdapter serieAdapter;
    private GoogleApiClient client;
    private Action action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNetworkAvaiable()) {
            initComponents();
        } else {
            showMessageError("Você não está conectado. Favor verificar conexão!");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        if (isNetworkAvaiable()) {
            connect();
            loadSeries();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (isNetworkAvaiable()) {
            disconnect();
        }
        super.onStop();
    }

    private void initComponents() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        action = Action.newAction(Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.mauricio.traktseries/http/host/path"));
        // exibir feedback de espera
        //spinner.setVisibility(View.VISIBLE);
    }

    private void connect() {
        client.connect();
        AppIndex.AppIndexApi.start(client, action);
    }

    private void disconnect() {
        AppIndex.AppIndexApi.end(client, action);
        client.disconnect();
    }

    @Background
    public void loadSeries() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(JSONUtils.URL, "https://api-v2launch.trakt.tv/shows/popular?extended=images");
        params.put(JSONUtils.CONTENT_TYPE, "application/json");
        params.put(JSONUtils.API_KEY, "f8c9aca99767c9802b8ab678ba327add9b3474f84f35cf2a2c993bb99fac7e6e");
        params.put(JSONUtils.API_VERSION, "2");
        JSONUtils.setApplicationContext(getApplicationContext());
        JSONUtils.makeRequest(this, params);
    }

    @Override
    public void onResponse(JSONArray response) {
        series = JSONUtils.convertFromJSONArrayToObject(response);
        serieAdapter = new SerieAdapter(this, series);
        recyclerView.setAdapter(serieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //spinner.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        //spinner.setVisibility(View.GONE);
        showMessageError(message);
    }

    private boolean isNetworkAvaiable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void showMessageError(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
