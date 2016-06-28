package activities.channels;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.RequestQuery;

public class ChannelsActivity extends AppCompatActivity {

    public static final String TAG = ChannelsActivity.class.getSimpleName();

    private ListView listView;
    private ChannelsAdapter adapter;
    private ArrayList<Channel> channelsList;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        channelsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        fetchChannels();
    }

    @Override
    protected void onStop(){
        savePreferences();
        super.onStop();
    }

    private void savePreferences(){
        if(channelsList.size() > 0) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            Set<String> set = new HashSet<>();
            for (Channel channel : channelsList) {
                if (channel.activo) {
                    set.add(channel._id);
                    FirebaseMessaging.getInstance().subscribeToTopic(channel._id);
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(channel._id);
                }
            }
            editor.putStringSet("channels", set);
            editor.commit();
        }
    }

    private List<String> loadPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("channels", null);
        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }
        return list;
    }

    private void updateListView(){
        adapter = new ChannelsAdapter(this, channelsList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void fetchChannels() {

        final List<String> savedChannels = loadPreferences();

        String url = getResources().getString(R.string.server_url) + "/getChannels";

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            channelsList = Channel.fromJson(response);
                            for(Channel channel : channelsList){
                                if(savedChannels.contains(channel._id)){
                                    channel.activo = true;
                                }
                            }
                            updateListView();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Server Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error actualizando los canales.", Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        RequestQuery.getInstance(this).addToRequestQueue(req);
    }

}
