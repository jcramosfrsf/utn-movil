package activities.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomás on 29/05/2016.
 */
public class New {

    public String autor;
    public String canal;
    public String titulo;
    public String cuerpo;
    public String fecha;
    public String imagen;

    // Constructor to convert JSON object into a Java class instance
    public New(JSONObject object){
        try {
            this.autor = object.getString("autor");
            this.canal = object.getString("canal");
            this.titulo = object.getString("titulo");
            this.cuerpo = object.getString("cuerpo");
            this.fecha = object.getString("fecha");
            this.imagen = object.getString("imagen");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // New.fromJson(jsonArray);
    public static ArrayList<New> fromJson(JSONArray jsonObjects) {
        ArrayList<New> news = new ArrayList<New>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                news.add(new New(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return news;
    }
}
