package activities.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import activities.channels.ChannelsActivity;
import utils.RequestQuery;

public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = NewsActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private NewsAdapter adapter;
    private ArrayList<New> newsList;
    private JSONArray jsonChannels;
    private boolean firstOnCreate;

    private int offSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.listView);
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(this, newsList);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                New noticia = newsList.get(position);
                Intent intent = new Intent(getBaseContext(), NewFull.class);
                intent.putExtra("noticia", noticia);
                startActivity(intent);
            }
        });

        firstOnCreate = true;
    }

    @Override
    public void onResume(){

        boolean needUpdate = loadPreferences();

        if(jsonChannels.length() == 0){
            clearNews();
            Toast.makeText(getApplicationContext(), "Pulsa en la esquina superior derecha para suscribirte a algún canal.", Toast.LENGTH_LONG).show();
        }else if(needUpdate || firstOnCreate){
            clearNews();
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            fetchNews();
                                        }
                                    }
            );
            firstOnCreate = false;
        }

        super.onResume();
    }

    private void clearNews(){
        offSet = 0;
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(this, newsList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        fetchNews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.channels_settings:
                Intent myIntent = new Intent(this, ChannelsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu, menu);
        return true;
    }

    private void fetchNews() {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JSONObject json = new JSONObject();
        try {
            json.put("channels", jsonChannels);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // appending offset to url
        String url = getResources().getString(R.string.server_url) + "/getNews?offset=" + offSet;
        Log.d(TAG, url);

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, url, json.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject newObj = response.getJSONObject(i);
                                    New m = new New(newObj);
                                    newsList.add(0, m);
                                    offSet = newsList.size();
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Server Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error actualizando las noticias.", Toast.LENGTH_LONG).show();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Adding request to request queue
        RequestQuery.getInstance(this).addToRequestQueue(req);
    }

    private boolean loadPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("channels", null);

        //Consume the update.
        boolean updatedChannels = sp.getBoolean("updatedChannels", false);
        if(updatedChannels){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("updatedChannels", false);
            editor.apply();
        }

        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }

        jsonChannels = new JSONArray(list);

        return updatedChannels;
    }

}
