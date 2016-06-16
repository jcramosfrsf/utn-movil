package activities.channels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tomasguti.utnmovil.R;

import org.json.JSONArray;

import java.util.ArrayList;

import utils.JSONStubs;

public class ChannelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        JSONArray jsonChannels = JSONStubs.getChannels();

        ArrayList<Channel> channels = Channel.fromJson(jsonChannels);
        ChannelsAdapter adapter = new ChannelsAdapter(this, channels);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
