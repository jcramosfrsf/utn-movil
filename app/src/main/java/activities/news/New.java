package activities.news;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
public class New implements Parcelable {

    public String autor;
    public String canal;
    public String titulo;
    public String cuerpo;
    public String imagen;
    public Date fecha;

    // Constructor to convert JSON object into a Java class instance
    public New(JSONObject object){
        try {
            this.autor = object.getString("autor");
            this.canal = object.getString("canal");
            this.titulo = object.getString("titulo");
            this.cuerpo = object.getString("cuerpo");
            this.imagen = object.getString("imagen");
            String stringFecha = object.getString("fecha");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
            fecha =  df.parse(stringFecha);
            Log.d("Date", fecha.toString());
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

    //Parceable Interface
    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(autor);
        out.writeString(canal);
        out.writeString(titulo);
        out.writeString(cuerpo);
        out.writeString(imagen);
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss z", Locale.ENGLISH);
        out.writeString(df.format(fecha));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<New> CREATOR = new Parcelable.Creator<New>() {
        public New createFromParcel(Parcel in) {
            return new New(in);
        }

        public New[] newArray(int size) {
            return new New[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private New (Parcel in) {
        this.autor = in.readString();
        this.canal = in.readString();
        this.titulo = in.readString();
        this.cuerpo = in.readString();
        this.imagen = in.readString();
        String stringFecha = in.readString();
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss z", Locale.ENGLISH);
        try {
            fecha =  df.parse(stringFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
