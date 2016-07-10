package activities.subjects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.util.ArrayList;

import activities.subjects.model.Comision;

/**
 * Created by Tomás on 29/05/2016.
 */
public class CommissionsAdapter extends ArrayAdapter<Comision> {
    public CommissionsAdapter(Context context, ArrayList<Comision> commissions) {
        super(context, 0, commissions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Comision newItem;
        newItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.commissions_container, parent, false);
        }
        // Lookup view for data population

        TextView name = (TextView) convertView.findViewById(R.id.name);
        Switch switch1 = (Switch) convertView.findViewById(R.id.switch1);

        // Populate the data into the template view using the data object
        name.setText(newItem.nombre);
        Log.d("SubjectsActivity", newItem.nombre + " " + newItem.activa);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newItem.activa = isChecked;
            }
        });
        switch1.setChecked(newItem.activa);

        // Return the completed view to render on screen
        return convertView;
    }
}
