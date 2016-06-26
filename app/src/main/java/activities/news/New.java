package activities.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tomás on 29/05/2016.
 */
public class New {

    public String autor;
    public String canal;
    public String titulo;
    public String cuerpo;
    public Date fecha;
    public String imagen;

    // Constructor to convert JSON object into a Java class instance
    public New(JSONObject object){
        try {
            this.autor = object.getString("autor");
            this.canal = object.getString("canal");
            this.titulo = object.getString("titulo");
            this.cuerpo = object.getString("cuerpo");
            this.imagen = object.getString("imagen");
            String stringFecha = object.getString("fecha");
            DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss z", Locale.ENGLISH);
            fecha =  df.parse(stringFecha);
        } catch (JSONException | ParseException e) {
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
