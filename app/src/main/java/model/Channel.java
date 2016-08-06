package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.utils.RequestQuery;

/**
 * Created by Tomas on 29/05/2016.
 * Clase que contiene la información de un canal. Además los trae del servidor y se encarga de su persistencia.
 */
public class Channel {

    public String _id;
    public String nombre;
    public String desc;
    public boolean activo;
    public static ArrayList<Channel> currents;
    public static List<String> saved;
    public boolean selected;

    public Channel(){
        activo = false;
        selected = false;
    }

    // Constructor to convert JSON object into a Java class instance
    public Channel(JSONObject object){
        activo = false;
        selected = false;
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
        ArrayList<Channel> channels = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                channels.add(new Channel(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return channels;
    }

    public static void loadFromServer(final Context context, final Callback callback){

        if(Channel.currents != null){
            callback.onSuccess();
            return;
        }

        String url = context.getResources().getString(R.string.server_url) + "/getChannels";

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.d(TAG, response.toString());
                if (response.length() > 0) {
                    Channel.currents = Channel.fromJson(response);
                    for(Channel channel : Channel.currents){
                        if(saved.contains(channel._id)){
                            channel.activo = true;
                        }
                    }
                    callback.onSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Server Error: " + error.getMessage());
                Toast.makeText(context, R.string.channels_error, Toast.LENGTH_LONG).show();
                callback.onError();
            }
        });

        // Adding request to request queue
        RequestQuery.getInstance(context).addToRequestQueue(req);
    }

    public static void savePreferences(Context context){
        if(Channel.currents !=null && Channel.currents.size() > 0) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            Set<String> set = new HashSet<>();
            for (Channel channel : Channel.currents) {
                if (channel.activo) {
                    set.add(channel._id);
                    FirebaseMessaging.getInstance().subscribeToTopic(channel._id);
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(channel._id);
                }
            }

            List<String> list = new ArrayList<>();
            list.addAll(set);

            if(compareChannels(list, saved)){
                editor.putBoolean("updatedChannels", false);
            }else{
                saved = list;
                editor.putBoolean("updatedChannels", true);
            }

            editor.putStringSet("channels", set);
            editor.apply();
        }
    }

    public static void loadPreferences(Context context){

        if(saved != null){
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = sp.getStringSet("channels", null);
        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }

        saved = list;
    }

    public static boolean compareChannels(List<String> arr1, List<String> arr2) {
        return arr1.containsAll(arr2) && arr2.containsAll(arr1);
    }
}
