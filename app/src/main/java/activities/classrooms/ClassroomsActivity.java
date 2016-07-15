package activities.classrooms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import activities.subjects.SubjectsActivity;
import activities.subjects.model.Comision;
import activities.subjects.model.Materia;

public class ClassroomsActivity extends AppCompatActivity {

    public static final String TAG = ClassroomsActivity.class.getSimpleName();

    private ArrayList<Classroom> classroomsArrayList;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView dateTextView;
    private static Date currentDate;
    private static boolean dateChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dateTextView = (TextView) findViewById(R.id.date);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onResume(){
        progressBar.setVisibility(View.VISIBLE);
        getMaterias();
        super.onResume();
    }

    public void getMaterias(){
        Materia.loadFromServer(this, getMateriasCallback);
    }

    public void updateMaterias(){

        if(Materia.actuales == null){
            return;
        }

        if(currentDate == null){
            currentDate = Calendar.getInstance().getTime();
        }

        SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEEE dd 'de' MMMMMMMMM 'de' yyyy", Locale.getDefault());

        String fullDate = fullDateFormat.format(currentDate);
        String upperCaseFullDate = fullDate.substring(0, 1).toUpperCase() + fullDate.substring(1);
        dateTextView.setText(upperCaseFullDate);
        String stringDate = serverDateFormat.format(currentDate);

        classroomsArrayList = new ArrayList<>();
        for(Materia materia : Materia.actuales){
            for(Comision comision : materia.comisiones){
                if(comision.activa){
                    if(comision.classroomCache == null || dateChanged){
                        Classroom classroom = new Classroom();
                        classroom.id_carrera = materia.id_carrera;
                        classroom.nombre = materia.nombre;
                        classroom.comision = comision.nombre;
                        classroomsArrayList.add(classroom);
                        classroom.loadDataFromServer(this, refreshDataCallback, stringDate, materia.id_carrera, materia.nivel, materia.id, comision.id);
                        comision.classroomCache = classroom;
                    }else{
                        classroomsArrayList.add(comision.classroomCache);
                    }
                }
            }
        }
        dateChanged = false;
        refreshList();
    }

    public void refreshList(){
        if(classroomsArrayList.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.suscribe_subjects, Toast.LENGTH_LONG).show();
        }
        ClassroomsAdapter classroomsAdapter = new ClassroomsAdapter(getApplicationContext(), classroomsArrayList);
        listView.setAdapter(classroomsAdapter);
    }

    public Callback getMateriasCallback = new Callback() {
        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);
            loadPreferences();
            updateMaterias();
        }

        @Override
        public void onError() {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.subjects_error, Toast.LENGTH_LONG).show();
        }
    };

    public Callback refreshDataCallback = new Callback() {
        @Override
        public void onSuccess() {
            refreshList();
        }

        @Override
        public void onError() {
            refreshList();
        }
    };

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
            case R.id.change_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return true;
            case R.id.force_update:
                dateChanged = true;
                updateMaterias();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            if(currentDate != null){
                c.setTime(currentDate);
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            currentDate = calendar.getTime();
            dateChanged = true;
            ((ClassroomsActivity) getActivity()).getMaterias();
        }
    }

    public void loadPreferences(){

        if(Materia.actuales == null){ return; }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("commissions", null);
        List<String> list = new ArrayList<>();
        if(set != null) {
            list.addAll(set);
        }

        for(String code : list){
            String[] parts = code.split("-", 3);
            int id_carrera = Integer.parseInt(parts[0]);
            int id_materia = Integer.parseInt(parts[1]);
            int id_comision = Integer.parseInt(parts[2]);
            for(Materia materia : Materia.actuales){
                if(materia.id_carrera == id_carrera && materia.id == id_materia){
                    for(Comision comision : materia.comisiones){
                        if(comision.id == id_comision){
                            comision.activa = comision.activaGuardada = true;
                            //Log.d(TAG, "Guardada: "+materia.nombre+ " " +comision.nombre);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}
