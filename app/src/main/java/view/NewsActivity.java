package view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import adapter.FilterSingleChannelListener;
import adapter.NewsAdapter;
import model.New;
import model.utils.RequestQuery;

public class NewsActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener{

    public static final String TAG = NewsActivity.class.getSimpleName();

    private SwipyRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private NewsAdapter adapter;
    private ArrayList<New> newsList;
    private JSONArray jsonChannels;
    private boolean firstOnCreate;
    private ChannelsSlidingMenu channelsSlidingMenu;

    private int offSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelsSlidingMenu.showMenu();
            }
        });

        String tag = "noticias";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(tag, 0);
        editor.apply();

        channelsSlidingMenu = new ChannelsSlidingMenu(this, filterSingleChannelListener);
        channelsSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        swipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.listView1);

        if(savedInstanceState != null){
            newsList = savedInstanceState.getParcelableArrayList("NEWS");

            int channelSelected = savedInstanceState.getInt("CHANNEL_SELECTED", 0);
            channelsSlidingMenu.illuminatePostion(channelSelected);
        }

        if(newsList == null){
            firstOnCreate = true;
            newsList = new ArrayList<>();
        }

        adapter = new NewsAdapter(this, newsList);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                New noticia = newsList.get(position);
                Intent intent = new Intent(getBaseContext(), NewFullActivity.class);
                intent.putExtra("noticia", noticia);
                startActivity(intent);
            }
        });

    }

    public FilterSingleChannelListener filterSingleChannelListener = new FilterSingleChannelListener() {

        @Override
        public void updateAllChannels() {
            updateNews(true);
            if(channelsSlidingMenu.isMenuShowing()) {
                channelsSlidingMenu.toggle();
            }
        }

        @Override
        public void updateSingleChannel(String channel) {
            List<String> list = new ArrayList<>();
            list.add(channel);
            jsonChannels = new JSONArray(list);
            clearNews();
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            fetchNews(SwipyRefreshLayoutDirection.TOP);
                                        }
                                    }
            );
            if(channelsSlidingMenu.isMenuShowing()) {
                channelsSlidingMenu.toggle();
            }
        }
    };

    @Override
    public void onResume(){
        updateNews(false);
        super.onResume();
    }

    private void updateNews(boolean force){
        boolean needUpdate = loadPreferences();

        if(jsonChannels.length() == 0){
            clearNews();
            Toast.makeText(getApplicationContext(), R.string.suscribe_channel, Toast.LENGTH_LONG).show();
        }else if(needUpdate || firstOnCreate || force){
            clearNews();
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            fetchNews(SwipyRefreshLayoutDirection.TOP);
                                        }
                                    }
            );
            firstOnCreate = false;
        }
    }

    private void clearNews(){
        offSet = 0;
        newsList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        fetchNews(direction);
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

    private void fetchNews(final SwipyRefreshLayoutDirection direction) {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JSONObject json = new JSONObject();
        try {
            json.put("channels", jsonChannels);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(direction == SwipyRefreshLayoutDirection.TOP){
            offSet = 0;
        }else{
            offSet = newsList.size();
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
                        if(direction == SwipyRefreshLayoutDirection.TOP){
                            newsList.clear();
                        }
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject newObj = response.getJSONObject(i);
                                New m = new New(newObj);
                                newsList.add(m);
                                offSet = newsList.size();
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.news_error, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(newsList != null){
            outState.putParcelableArrayList("NEWS", newsList);
        }

        if(channelsSlidingMenu != null){
            outState.putInt("CHANNEL_SELECTED", channelsSlidingMenu.getSelectedPosition());
        }
    }
}
