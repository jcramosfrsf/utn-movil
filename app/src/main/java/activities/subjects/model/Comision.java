package activities.subjects.model;

import org.json.JSONException;
import org.json.JSONObject;

import activities.classrooms.Classroom;

/**
 * Created by Tomas on 09/07/2016.
 * Objecto que guarda la información de una comisión. Se crea desde un JSON.
 */
public class Comision {
    public int id;
    public String nombre;
    public boolean activaGuardada;
    public boolean activa;
    public Classroom classroomCache;

    public Comision(JSONObject object){
        activa = false;
        activaGuardada = false;
        try {
            this.id = object.getInt("id");
            this.nombre = object.getString("nombre");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
