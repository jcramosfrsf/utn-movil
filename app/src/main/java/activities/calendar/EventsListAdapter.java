package activities.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Tomas on 29/05/2016.
 */
public class EventsListAdapter extends ArrayAdapter<CalendarEvent> {
    public EventsListAdapter(Context context, ArrayList<CalendarEvent> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CalendarEvent eventItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_container, parent, false);
        }
        // Lookup view for data population
        TextView hourView = (TextView) convertView.findViewById(R.id.hour);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView place = (TextView) convertView.findViewById(R.id.place);

        //SimpleDateFormat df = new SimpleDateFormat("EEEE dd 'de' MMMMMMMMM 'de' yyyy");
        SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
        String hs = hf.format(eventItem.fecha);

        // Populate the data into the template view using the data object
        title.setText(eventItem.titulo);
        hourView.setText("Hora: "+hs);
        place.setText("Lugar: "+eventItem.lugar);

        // Return the completed view to render on screen
        return convertView;
    }
}
