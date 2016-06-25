package activities.channels;

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

import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import java.util.ArrayList;
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
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        fetchChannels();
    }

    private void updateListView(){
        adapter = new ChannelsAdapter(this, channelsList);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private void fetchChannels() {

        String url = getResources().getString(R.string.server_url) + "/getChannels";

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            channelsList = Channel.fromJson(response);
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
