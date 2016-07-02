package activities.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.tomasguti.utnmovil.R;

import java.util.ArrayList;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CalendarAdapter extends CaldroidGridAdapter {

    public CalendarAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);
        ImageView circle = (ImageView) cellView.findViewById(R.id.circle);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        //Get events from external source
        Map<Integer, Integer> events = (Map) extraData.get("EVENTS");

        int numberOfEvents = 0;
        if(events != null){
            int dayNumber = dateTime.getDay();
            if(events.containsKey(dayNumber)){
                numberOfEvents = events.get(dateTime.getDay());
            }
        }

        tv1.setTextColor(Color.BLACK);
        tv2.setVisibility(View.INVISIBLE);
        circle.setVisibility(View.INVISIBLE);
        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(ContextCompat.getColor(context, R.color.caldroid_darker_gray));
            tv2.setVisibility(View.INVISIBLE);
            circle.setVisibility(View.INVISIBLE);
        }else if(numberOfEvents > 0){
            tv2.setText(""+numberOfEvents);
            tv2.setVisibility(View.VISIBLE);
            circle.setVisibility(View.VISIBLE);
        }
/*
        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);

            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }
*/

        // Customize for selected dates
        /*
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(ContextCompat.getColor(context, com.caldroid.R.color.caldroid_sky_blue));
        } else {
            shouldResetSelectedView = true;
        }*/
        /*
        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(R.drawable.cell_bg);
            }
        }*/

        cellView.setBackgroundResource(R.drawable.cell_bg);

        tv1.setText("" + dateTime.getDay());

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }

}
