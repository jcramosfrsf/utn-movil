package activities.subjects.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tomás on 09/07/2016.
 */
public class Comision {
    public int id;
    public String nombre;
    public boolean activaGuardada;
    public boolean activa;

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
