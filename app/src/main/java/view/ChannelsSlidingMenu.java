package view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import java.util.ArrayList;

import adapter.FilterSingleChannelListener;
import model.Channel;

/**
 * Created by Tomas on 16/07/2016.
 */
public class ChannelsSlidingMenu extends SlidingMenu {

    private ListView listView;
    private ChannelsMenuAdapter adapter;

    public ChannelsSlidingMenu(Context context, final FilterSingleChannelListener listener) {
        super(context);
        setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        setMode(SlidingMenu.SLIDING_WINDOW);
        setShadowWidthRes(R.dimen.shadow_width_slidingmenu);
        setShadowDrawable(R.drawable.shadow_slidingmenu);
        setBehindOffsetRes(R.dimen.slidingmenu_offset);
        setMenu(R.layout.slidingmenu_channels);

        listView = (ListView) findViewById(R.id.listViewChannels);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                illuminatePostion(position);
                if(position == 0){
                    listener.updateAllChannels();
                }else{
                    Channel singleChannel =  adapter.getItem(position);
                    String channelName = singleChannel._id;
                    listener.updateSingleChannel(channelName);
                }
            }
        });

        Channel.loadPreferences(context);
        Channel.loadFromServer(context, callback);
    }

    public void illuminatePostion(int position){

        if(adapter == null){
            return;
        }

        //Reset Selected
        for(int i=0; i < adapter.getCount(); i++){
            Channel channel = adapter.getItem(i);
            channel.selected = false;
        }

        if(position == 0){
            adapter.getItem(0).selected = true;
        }else{
            Channel singleChannel =  adapter.getItem(position);
            singleChannel.selected = true;
        }
        adapter.notifyDataSetInvalidated();
    }

    public int getSelectedPosition(){
        if(adapter == null){
            return 0;
        }
        for(int i=0; i < adapter.getCount(); i++){
            Channel channel = adapter.getItem(i);
            if(channel.selected) {
                return i;
            }
        }
        return 0;
    }

    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {
            ArrayList<Channel> channelArrayList = new ArrayList<>();
            Channel allChannelsOption = new Channel();
            allChannelsOption.nombre = getResources().getString(R.string.my_channels);
            allChannelsOption.selected = true;
            channelArrayList.add(0, allChannelsOption);
            //Reset Selected
            for(Channel channel : Channel.currents){
                channel.selected = false;
            }
            channelArrayList.addAll(Channel.currents);
            adapter = new ChannelsMenuAdapter(getContext(), channelArrayList);
            listView.setAdapter(adapter);
        }

        @Override
        public void onError() {

        }
    };

    class ChannelsMenuAdapter extends ArrayAdapter<Channel> {

        public ChannelsMenuAdapter(Context context, ArrayList<Channel> channels) {
            super(context, 0, channels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Channel newItem;
            newItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.slidingmenu_channels_container, parent, false);
            }
            // Lookup view for data population

            TextView nombre = (TextView) convertView.findViewById(R.id.title);
            View background = convertView.findViewById(R.id.background_layout);

            // Populate the data into the template view using the data object
            nombre.setText(newItem.nombre);

            if(newItem.selected){
                background.setBackgroundResource(R.color.caldroid_holo_blue_light);
            }else{
                background.setBackgroundResource(R.color.caldroid_transparent);
            }

            // Return the completed view to render on screen
            return convertView;
        }

    }
}
