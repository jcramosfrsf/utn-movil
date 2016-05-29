package activities.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.util.ArrayList;

/**
 * Created by Tomás on 29/05/2016.
 */
public class NewsAdapter extends ArrayAdapter<New> {
    public NewsAdapter(Context context, ArrayList<New> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        New newItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_container, parent, false);
        }
        // Lookup view for data population
        TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView body = (TextView) convertView.findViewById(R.id.body);

        // Populate the data into the template view using the data object
        author.setText(newItem.author);
        title.setText(newItem.title);
        body.setText(newItem.body);

        // Return the completed view to render on screen
        return convertView;
    }
}
