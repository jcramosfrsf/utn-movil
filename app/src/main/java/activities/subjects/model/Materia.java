package activities.subjects.model;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

import utils.RequestQuery;

/**
 * Created by Tomas on 09/07/2016.
 * Objecto con la información de una materia.
 * La clase además se encarga de traerlas del servidor y parsearlas.
 */
public class Materia {
    public int id;
    public int id_carrera;
    public int nivel;
    public String nombre;
    public ArrayList<Comision> comisiones;

    public static ArrayList<Materia> actuales;

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

    public static void loadFromServer(Context context, final Callback callback){

        if(actuales != null){
            callback.onSuccess();
            return;
        }

        String url = context.getResources().getString(R.string.utn_server_url) + "/getMaterias.php";

        JsonObjectRequest request = new JsonObjectRequest(url,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                actuales = fromJson(response);
                callback.onSuccess();
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
            }
        });

        RequestQuery.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public String toString(){
        return nombre;
    }
}
