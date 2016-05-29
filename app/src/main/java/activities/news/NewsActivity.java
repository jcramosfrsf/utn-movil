package activities.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import utils.JSONNews;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        JSONArray jsonNews = new JSONArray();

        try{
            jsonNews = new JSONNews();
        }catch(JSONException e){
            e.printStackTrace();
        }

        ArrayList<New> news = New.fromJson(jsonNews);
        NewsAdapter adapter = new NewsAdapter(this, news);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
