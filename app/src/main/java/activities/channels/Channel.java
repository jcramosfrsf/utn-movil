package activities.channels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomas on 29/05/2016.
 */
public class Channel {

    public String _id;
    public String nombre;
    public String desc;
    public boolean activo;

    // Constructor to convert JSON object into a Java class instance
    public Channel(JSONObject object){
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
    public static ArrayList<Channel> fromJson(JSONArray jsonObjects) {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                channels.add(new Channel(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return channels;
    }
}
