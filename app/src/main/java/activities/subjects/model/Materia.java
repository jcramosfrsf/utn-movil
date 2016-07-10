package activities.subjects.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tomás on 09/07/2016.
 */
public class Materia {
    public int id;
    public int id_carrera;
    public int nivel;
    public String nombre;
    public ArrayList<Comision> comisiones;

    // Constructor to convert JSON object into a Java class instance
    public Materia(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.id_carrera = object.getInt("id_carrera");
            this.nivel = object.getInt("nivel");
            this.nombre = object.getString("nombre");
            comisiones = new ArrayList<>();
            JSONObject jsonComisiones = object.getJSONObject("comisiones");
            Iterator<String> iterator = jsonComisiones.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONObject value = (JSONObject) jsonComisiones.get(key);
                Comision comision = new Comision(value);
                comisiones.add(comision);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Materia> fromJson(JSONObject jsonObjects) {
        ArrayList<Materia> materias = new ArrayList<>();
        Iterator<String> iterator = jsonObjects.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject value = null;
            try {
                value = (JSONObject) jsonObjects.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Materia materia = new Materia(value);
            materias.add(materia);
        }
        return materias;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
