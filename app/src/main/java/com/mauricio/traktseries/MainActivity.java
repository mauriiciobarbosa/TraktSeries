package com.mauricio.traktseries;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Action;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mauricio.traktseries.model.Serie;
import com.mauricio.traktseries.model.SerieAdapter;
import com.mauricio.traktseries.utils.JSONUtils;
import com.mauricio.traktseries.model.ObserverJSON;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements ObserverJSON {

    private static final int COLUMNS_NUMBER = 3;

    @ViewById(R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewById(R.id.progress)
    public ProgressBar progressBar;

    private List<Serie> series;
    private SerieAdapter serieAdapter;
    private GoogleApiClient client;
    private Action action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNetworkAvailable()) {
            initComponents();
        } else {
            showMessageError(getString(R.string.error_connection));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            connect();
            loadSeries();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (isNetworkAvailable()) {
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
                getString(R.string.main_page),
                Uri.parse(getString(R.string.host_pah)),
                Uri.parse(getString(R.string.android_app_host_path)));
        JSONUtils.setApplicationContext(getApplicationContext());
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
        Map<String, String> params = new HashMap<>();
        params.put(JSONUtils.URL, getString(R.string.api_url));
        params.put(JSONUtils.CONTENT_TYPE, getString(R.string.api_content_type));
        params.put(JSONUtils.API_KEY, getString(R.string.api_key));
        params.put(JSONUtils.API_VERSION, getString(R.string.api_version));
        JSONUtils.makeRequest(this, params);
    }

    @Override
    public void onResponse(JSONArray response) {
        series = JSONUtils.convertFromJSONArrayToObject(response);
        serieAdapter = new SerieAdapter(this, series);
        recyclerView.setAdapter(serieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMNS_NUMBER));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(int idMessage) {
        progressBar.setVisibility(View.GONE);
        showMessageError(getString(idMessage));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void showMessageError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
