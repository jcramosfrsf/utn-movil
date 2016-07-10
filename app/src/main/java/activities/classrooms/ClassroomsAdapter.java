package activities.classrooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasguti.utnmovil.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import activities.news.New;

/**
 * Created by Tomás on 29/05/2016.
 */
public class ClassroomsAdapter extends ArrayAdapter<Classroom> {
    public ClassroomsAdapter(Context context, ArrayList<Classroom> classrooms) {
        super(context, 0, classrooms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Classroom newItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.classrooms_container, parent, false);
        }

        // Lookup view for data population
        TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        TextView comision = (TextView) convertView.findViewById(R.id.comision);
        TextView aula = (TextView) convertView.findViewById(R.id.aula);
        TextView horario = (TextView) convertView.findViewById(R.id.horario);

        nombre.setText(newItem.nombre);
        comision.setText(newItem.comision);
        aula.setText(newItem.aula);
        horario.setText(newItem.horario);

        // Return the completed view to render on screen
        return convertView;
    }
}
