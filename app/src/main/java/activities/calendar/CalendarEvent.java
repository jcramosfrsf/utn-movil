package activities.calendar;

import android.os.Parcel;
import android.os.Parcelable;

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
public class CalendarEvent implements Parcelable{

    public String canal;
    public String titulo;
    public String lugar;
    public Date fecha;

    // Constructor to convert JSON object into a Java class instance
    public CalendarEvent(JSONObject object){
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

    //Parceable Interface
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(canal);
        out.writeString(titulo);
        out.writeString(lugar);
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss z", Locale.ENGLISH);
        out.writeString(df.format(fecha));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<CalendarEvent> CREATOR = new Parcelable.Creator<CalendarEvent>() {
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private CalendarEvent (Parcel in) {
        this.canal = in.readString();
        this.titulo = in.readString();
        this.titulo = in.readString();
        this.lugar = in.readString();
        String stringFecha = in.readString();
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss z", Locale.ENGLISH);
        try {
            fecha =  df.parse(stringFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
