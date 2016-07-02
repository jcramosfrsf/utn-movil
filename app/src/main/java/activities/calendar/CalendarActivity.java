package activities.calendar;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.tomasguti.utnmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.RequestQuery;

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = CalendarActivity.class.getSimpleName();

    //private ListView listView;
    //private NewsAdapter adapter;
    private ArrayList<CalendarEvent> eventsList;
    private CaldroidFragment caldroidFragment;
    Calendar cal;
    private String jsonChannelsString;
    Map<Integer, Integer> eventsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        List<String> channels = loadPreferences();
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(channels);
        try {
            json.put("channels", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonChannelsString = json.toString();

        caldroidFragment = new CustomCalendar();
        cal = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);

        //caldroidFragment.setTextColorForDate(Color.BLUE, cal.getTime());
        caldroidFragment.setArguments(args);
        caldroidFragment.setTextColorForDate(R.color.currentDateColor, cal.getTime());

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), date.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                eventsMap = new HashMap<>();
                Map<String, Object> extraData = caldroidFragment.getExtraData();
                extraData.put("EVENTS", eventsMap);
                caldroidFragment.refreshView();
                fetchEvents(month, year);
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), "LONG"+date.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                /*
                int month = caldroidFragment.getMonth();
                int year = caldroidFragment.getYear();
                fetchEvents(month, year);*/
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    public List<String> loadPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("channels", null);
        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }
        return list;
    }

    private void fetchEvents(int month, int year) {

        int javaMonth = month - 1;
        String url = getResources().getString(R.string.server_url) + "/getEvents?month="+javaMonth+"&year="+year;
        //Log.d(TAG, url);

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, url, jsonChannelsString,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            eventsList = CalendarEvent.fromJson(response);
                            Calendar calendar = Calendar.getInstance();
                            for(CalendarEvent event : eventsList){
                                calendar.setTime(event.fecha);
                                int dayIndex = calendar.get(Calendar.DAY_OF_MONTH);
                                if (eventsMap.containsKey(dayIndex)) {
                                    eventsMap.put(dayIndex, eventsMap.get(dayIndex)+1);
                                }else{
                                    eventsMap.put(dayIndex, 1);
                                }
                            }
                            Map<String, Object> extraData = caldroidFragment.getExtraData();
                            extraData.put("EVENTS", eventsMap);
                            caldroidFragment.refreshView();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Server Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error actualizando los eventos.", Toast.LENGTH_LONG).show();
            }
        });
        // Adding request to request queue
        RequestQuery.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}
