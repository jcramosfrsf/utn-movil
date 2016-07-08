package activities.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import activities.channels.ChannelsActivity;
import utils.RequestQuery;

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = CalendarActivity.class.getSimpleName();

    private EventsListAdapter eventsListAdapter;
    private ArrayList<CalendarEvent> dayEventsList; //For the adapter
    private ArrayList<CalendarEvent> eventsList;
    private ListView eventsListView;
    private View eventsFull;
    private TextView eventsFullDate;
    private CaldroidFragment caldroidFragment;
    Calendar cal;
    private JSONArray jsonChannels;
    Map<Integer, Integer> eventsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        loadPreferences();

        caldroidFragment = new CustomCalendar();
        cal = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);

        caldroidFragment.setArguments(args);
        caldroidFragment.setTextColorForDate(R.color.currentDateColor, cal.getTime());

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        //Setups Day Popup Adapter
        eventsFull = (View) findViewById(R.id.eventsFull);
        eventsFullDate = (TextView) findViewById(R.id.date);
        eventsListView = (ListView) findViewById(R.id.eventsListView);
        dayEventsList = new ArrayList<>();
        eventsListAdapter = new EventsListAdapter(this, dayEventsList);
        eventsListView.setAdapter(eventsListAdapter);

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                //Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
                int selectedMonth = calendar.get(Calendar.MONTH);

                dayEventsList = new ArrayList<>();
                for(CalendarEvent event : eventsList){
                    calendar.setTime(event.fecha);
                    int arrayDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int arrayMonth = calendar.get(Calendar.MONTH);
                    if(selectedDay == arrayDay && selectedMonth == arrayMonth){
                        dayEventsList.add(event);
                    }
                }
               if(dayEventsList.size() > 0){
                   SimpleDateFormat df = new SimpleDateFormat("EEEE dd 'de' MMMMMMMMM 'de' yyyy");
                   String fullDateString = df.format(date);
                   String upperCaseFullDate = fullDateString.substring(0, 1).toUpperCase() + fullDateString.substring(1);
                   eventsFullDate.setText(upperCaseFullDate);
                   eventsListAdapter = new EventsListAdapter(getApplicationContext(), dayEventsList);
                   eventsListView.setAdapter(eventsListAdapter);
                   eventsFull.setVisibility(View.VISIBLE);
               }else{
                   eventsFull.setVisibility(View.INVISIBLE);
               }

            }

            @Override
            public void onChangeMonth(int month, int year) {
                clearEvents();
                fetchEvents(month, year);
            }
            /*
            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), date.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {

            }
            */
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    @Override
    public void onResume(){

        boolean needUpdate = loadPreferences();

        if(jsonChannels.length() == 0){
            Toast.makeText(getApplicationContext(), "Pulsa en la esquina superior derecha para suscribirte a algún canal.", Toast.LENGTH_LONG).show();
            clearEvents();
        }else if(needUpdate){
            caldroidFragment.getCaldroidListener().onChangeMonth(caldroidFragment.getMonth(), caldroidFragment.getYear());
        }

        super.onResume();
    }

    private void clearEvents(){
        eventsMap = new HashMap<>();
        Map<String, Object> extraData = caldroidFragment.getExtraData();
        extraData.put("EVENTS", eventsMap);
        caldroidFragment.refreshView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.channels_settings:
                Intent myIntent = new Intent(this, ChannelsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_menu, menu);
        return true;
    }

    public void hideEventsFull(View v){
        eventsFull.setVisibility(View.INVISIBLE);
    }

    private void fetchEvents(int month, int year) {

        int javaMonth = month - 1;
        String url = getResources().getString(R.string.server_url) + "/getEvents?month="+javaMonth+"&year="+year;
        Log.d(TAG, url);
        JSONObject json = new JSONObject();
        try {
            json.put("channels", jsonChannels);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, url, json.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
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

    private boolean loadPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sp.getStringSet("channels", null);

        //Consume the update.
        boolean updatedChannels = sp.getBoolean("updatedChannels", false);
        if(updatedChannels){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("updatedChannels", false);
            editor.apply();
        }

        List<String> list = new ArrayList<>();
        if(set != null){
            list.addAll(set);
        }

        jsonChannels = new JSONArray(list);

        return updatedChannels;
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}
