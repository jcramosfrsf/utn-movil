package activities.subjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import activities.subjects.model.Comision;
import activities.subjects.model.Materia;

public class SubjectsActivity extends AppCompatActivity {

    public static final String TAG = SubjectsActivity.class.getSimpleName();

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter autoCompleteAdapter;
    private ArrayList<Materia> materiasDeCarrera;
    private Spinner spinner;
    private ListView listViewCommisions;
    private CommissionsAdapter commissionsAdapter;

    private TextView textViewMateria;
    private TextView textViewComision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        materiasDeCarrera = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.careers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setEnabled(false);

        textViewMateria = (TextView) findViewById(R.id.textViewMateria);
        textViewComision = (TextView) findViewById(R.id.textViewComision);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        listViewCommisions = (ListView) findViewById(R.id.listViewCommisions);

        ArrayList<Comision> comisiones = new ArrayList<>();
        commissionsAdapter = new CommissionsAdapter(getApplicationContext(), comisiones);

        textViewMateria.setVisibility(View.INVISIBLE);
        textViewComision.setVisibility(View.INVISIBLE);
        listViewCommisions.setVisibility(View.INVISIBLE);
        autoCompleteTextView.setVisibility(View.INVISIBLE);

        Materia.loadFromServer(this, callbackGetMaterias);
    }

    private Callback callbackGetMaterias = new Callback() {
        @Override
        public void onSuccess() {
            spinner.setEnabled(true);
            loadPreferences();
            spinner.setOnItemSelectedListener(new SpinnerListener());
            autoCompleteTextView.setOnItemClickListener(new AutoCompleteListener());
            autoCompleteTextView.setOnFocusChangeListener(new AutoCompleteOnFocusListener());
            autoCompleteTextView.setThreshold(1);
        }

        @Override
        public void onError() {
            Toast.makeText(getApplicationContext(), R.string.subjects_error, Toast.LENGTH_LONG).show();
        }
    };

    private class AutoCompleteOnFocusListener implements AdapterView.OnFocusChangeListener{
        public void onFocusChange(View view, boolean focus) {
            if(focus) {
                textViewComision.setVisibility(View.INVISIBLE);
                listViewCommisions.setVisibility(View.INVISIBLE);
            }else{
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            }
        }
    }

    private class AutoCompleteListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            Materia materia = (Materia) autoCompleteAdapter.getItem(pos);
            Log.d(TAG, materia.nombre+ " " + pos);

            commissionsAdapter = new CommissionsAdapter(getApplicationContext(), materia.comisiones);
            listViewCommisions.setAdapter(commissionsAdapter);

            autoCompleteTextView.clearFocus();
            textViewComision.setVisibility(View.VISIBLE);
            listViewCommisions.setVisibility(View.VISIBLE);
        }
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(pos != 0){
                String careerCodeString = getResources().getStringArray(R.array.careers_array_id)[pos];
                int careerCode = Integer.parseInt(careerCodeString);
                materiasDeCarrera = new ArrayList<>();
                for(Materia materia : Materia.actuales){
                    if(materia.id_carrera == careerCode){
                        materiasDeCarrera.add(materia);
                    }
                }
                autoCompleteAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.subjects_container, R.id.item, materiasDeCarrera);
                autoCompleteTextView.setAdapter(autoCompleteAdapter);
                textViewMateria.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                textViewComision.setVisibility(View.INVISIBLE);
                listViewCommisions.setVisibility(View.INVISIBLE);
            }else{
                textViewMateria.setVisibility(View.INVISIBLE);
                autoCompleteTextView.setVisibility(View.INVISIBLE);
                textViewComision.setVisibility(View.INVISIBLE);
                listViewCommisions.setVisibility(View.INVISIBLE);
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    @Override
    protected void onPause(){
        savePreferences();
        super.onPause();
    }

    private void savePreferences(){

        if(Materia.actuales == null){
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = new HashSet<>();
        for (Materia materia : Materia.actuales) {
            for(Comision comision : materia.comisiones){
                String code = materia.id_carrera+"-"+materia.id+"-"+comision.id;
                if(comision.activa != comision.activaGuardada){
                    if (comision.activa) {
                        Log.d(TAG, "subscribeToTopic:"+code);
                        //FirebaseMessaging.getInstance().subscribeToTopic(code);
                    }else{
                        Log.d(TAG, "unsubscribeFromTopic:"+code);
                        //FirebaseMessaging.getInstance().unsubscribeFromTopic(code);
                    }
                    comision.activaGuardada = comision.activa;
                }
                if (comision.activa && comision.activaGuardada) {
                    set.add(code);
                }
            }
        }
        editor.putStringSet("commissions", set);
        editor.apply();
    }

    public void loadPreferences(){

        if(Materia.actuales == null){
            return;
        }

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
            for(Materia materia : Materia.actuales){
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
