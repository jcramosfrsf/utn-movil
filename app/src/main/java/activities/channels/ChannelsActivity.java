package activities.channels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

public class ChannelsActivity extends AppCompatActivity {

    public static final String TAG = ChannelsActivity.class.getSimpleName();

    private ListView listView;
    private ChannelsAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        Channel.loadPreferences(this);

        listView = (ListView) findViewById(R.id.listView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Channel.loadFromServer(this, callback);
    }

    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {
            updateListView();
        }

        @Override
        public void onError() {
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onPause(){
        Channel.savePreferences(this);
        super.onPause();
    }

    private void updateListView(){
        listView.setVisibility(View.VISIBLE);
        adapter = new ChannelsAdapter(this, Channel.currents);
        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

}
