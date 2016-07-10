package activities.classrooms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.tomasguti.utnmovil.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import activities.channels.ChannelsActivity;
import activities.subjects.SubjectsActivity;
import activities.subjects.model.Comision;
import activities.subjects.model.Materia;
import utils.JSONStubs;

public class ClassroomsActivity extends AppCompatActivity {

    public static final String TAG = SubjectsActivity.class.getSimpleName();

    private ArrayList<Classroom> classroomsArrayList;
    private ListView listView;
    private ClassroomsAdapter classroomsAdapter;
    private ArrayList<Materia> materias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms);

        JSONObject json = JSONStubs.getMaterias();
        materias = Materia.fromJson(json);

        loadPreferences();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        classroomsArrayList = new ArrayList<>();

        for(Materia materia : materias){
            for(Comision comision : materia.comisiones){
                if(comision.activa){
                    Classroom classroom = new Classroom();
                    classroom.nombre = materia.nombre;
                    classroom.comision = comision.nombre;
                    classroomsArrayList.add(classroom);
                }
            }
        }

        listView = (ListView) findViewById(R.id.listView);
        classroomsAdapter = new ClassroomsAdapter(this, classroomsArrayList);
        listView.setAdapter(classroomsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.classrooms_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subjects_settings:
                Intent myIntent = new Intent(this, SubjectsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("commissions", null);
        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }
        for(String code : list){
            String[] parts = code.split("-", 3);
            int id_carrera = Integer.parseInt(parts[0]);
            int id_materia = Integer.parseInt(parts[1]);
            int id_comision = Integer.parseInt(parts[2]);
            for(Materia materia : materias){
                if(materia.id_carrera == id_carrera && materia.id == id_materia){
                    for(Comision comision : materia.comisiones){
                        if(comision.id == id_comision){
                            comision.activa = comision.activaGuardada = true;
                            Log.d(TAG, "Guardada: "+materia.nombre+ " " +comision.nombre);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
