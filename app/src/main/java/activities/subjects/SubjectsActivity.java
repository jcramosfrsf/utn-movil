package activities.subjects;

import android.content.Context;
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

import com.tomasguti.utnmovil.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import activities.subjects.model.Comision;
import activities.subjects.model.Materia;
import utils.JSONStubs;

public class SubjectsActivity extends AppCompatActivity {

    public static final String TAG = SubjectsActivity.class.getSimpleName();

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter autoCompleteAdapter;
    private ArrayList<Materia> materias;
    private ArrayList<Materia> materiasDeCarrera;
    private Spinner spinner;
    private ListView listViewCommisions;
    private CommissionsAdapter commissionsAdapter;

    private TextView textViewCarrera;
    private TextView textViewMateria;
    private TextView textViewComision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        materiasDeCarrera = new ArrayList<>();

        JSONObject json = JSONStubs.getMaterias();
        materias = Materia.fromJson(json);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.careers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerListener());

        textViewCarrera = (TextView) findViewById(R.id.textViewCarrera);
        textViewMateria = (TextView) findViewById(R.id.textViewMateria);
        textViewComision = (TextView) findViewById(R.id.textViewComision);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setOnItemClickListener(new AutoCompleteListener());
        autoCompleteTextView.setOnFocusChangeListener(new AutoCompleteOnFocusListener());
        autoCompleteTextView.setThreshold(1);

        listViewCommisions = (ListView) findViewById(R.id.listViewCommisions);

        ArrayList<Comision> comisiones = new ArrayList<>();
        commissionsAdapter = new CommissionsAdapter(getApplicationContext(), comisiones);

        textViewMateria.setVisibility(View.INVISIBLE);
        textViewComision.setVisibility(View.INVISIBLE);
        listViewCommisions.setVisibility(View.INVISIBLE);
        autoCompleteTextView.setVisibility(View.INVISIBLE);
    }

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

        public void onNothingSelected(AdapterView<?> parent) {
            textViewComision.setVisibility(View.INVISIBLE);
            listViewCommisions.setVisibility(View.INVISIBLE);
        }
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using

            if(pos != 0){
                String careerCodeString = getResources().getStringArray(R.array.careers_array_id)[pos];
                int careerCode = Integer.parseInt(careerCodeString);
                materiasDeCarrera = new ArrayList<>();
                for(Materia materia : materias){
                    if(materia.id_carrera == careerCode){
                        materiasDeCarrera.add(materia);
                    }
                }
                autoCompleteAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.subjects_container, R.id.item, materiasDeCarrera);
                autoCompleteTextView.setAdapter(autoCompleteAdapter);
                textViewMateria.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
            }else{
                textViewMateria.setVisibility(View.INVISIBLE);
                autoCompleteTextView.setVisibility(View.INVISIBLE);
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

}
