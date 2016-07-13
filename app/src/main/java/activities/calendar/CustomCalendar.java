package activities.calendar;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Tomas on 01/07/2016.
 */
public class CustomCalendar extends CaldroidFragment {
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CalendarAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }
}
