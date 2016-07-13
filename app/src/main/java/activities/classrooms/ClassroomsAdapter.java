package activities.classrooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.util.ArrayList;

/**
 * Created by Tomas on 29/05/2016.
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
        TextView carrera = (TextView) convertView.findViewById(R.id.carrera);
        TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        TextView comision = (TextView) convertView.findViewById(R.id.comision);
        TextView aula = (TextView) convertView.findViewById(R.id.aula);
        TextView horario = (TextView) convertView.findViewById(R.id.horario);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);

        String[] stringsCarreras = getContext().getResources().getStringArray(R.array.careers_array);
        String carreraString = "";
        switch(newItem.id_carrera){
            case 1:
                carreraString = stringsCarreras[1];
                break;
            case 2:
                carreraString = stringsCarreras[2];
                break;
            case 5:
                carreraString = stringsCarreras[3];
                break;
            case 6:
                carreraString = stringsCarreras[4];
                break;
            case 7:
                carreraString = stringsCarreras[5];
                break;
            case 8:
                carreraString = stringsCarreras[6];
                break;
            case 9:
                carreraString = stringsCarreras[7];
                break;
            case 10:
                carreraString = stringsCarreras[8];
                break;
        }

        carrera.setText(carreraString);

        nombre.setText(newItem.nombre);
        comision.setText(newItem.comision);
        aula.setText(newItem.aula);
        horario.setText(newItem.horario);
        if(newItem.horario != null){
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
