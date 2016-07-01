package activities.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomás on 29/05/2016.
 */
public class CalendarEvent {

    public String _id;
    public String nombre;
    public String desc;
    public boolean activo;

    // Constructor to convert JSON object into a Java class instance
    public CalendarEvent(JSONObject object){
        activo = false;
        try {
            this._id = object.getString("_id");
            this.nombre = object.getString("nombre");
            this.desc = object.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // New.fromJson(jsonArray);
    public static ArrayList<CalendarEvent> fromJson(JSONArray jsonObjects) {
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
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
