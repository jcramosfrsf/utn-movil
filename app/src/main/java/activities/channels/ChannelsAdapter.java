package activities.channels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.util.ArrayList;

import activities.news.New;

/**
 * Created by Tomás on 29/05/2016.
 */
public class ChannelsAdapter extends ArrayAdapter<Channel> {
    public ChannelsAdapter(Context context, ArrayList<Channel> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Channel newItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channels_container, parent, false);
        }
        // Lookup view for data population

        TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);

        // Populate the data into the template view using the data object
        nombre.setText(newItem.nombre);
        desc.setText(newItem.desc);

        // Return the completed view to render on screen
        return convertView;
    }
}
