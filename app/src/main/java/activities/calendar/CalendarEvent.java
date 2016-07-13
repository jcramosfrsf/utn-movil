package activities.calendar;

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
 * Created by Tomas on 29/05/2016.
 */
public class CalendarEvent {

    public String canal;
    public String titulo;
    public String lugar;
    public Date fecha;
    public boolean activo;

    // Constructor to convert JSON object into a Java class instance
    public CalendarEvent(JSONObject object){
        activo = false;
        try {
            this.canal = object.getString("canal");
            this.titulo = object.getString("titulo");
            this.lugar = object.getString("lugar");
            String stringFecha = object.getString("fecha");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", Locale.getDefault());
            fecha =  df.parse(stringFecha);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // New.fromJson(jsonArray);
    public static ArrayList<CalendarEvent> fromJson(JSONArray jsonObjects) {
        ArrayList<CalendarEvent> events = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                events.add(new CalendarEvent(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }
}
