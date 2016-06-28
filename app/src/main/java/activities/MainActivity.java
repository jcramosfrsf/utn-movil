/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tomasguti.utnmovil.R;

import activities.calendar.CalendarActivity;
import activities.channels.ChannelsActivity;
import activities.news.NewsActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showPopup(View view) {
        View anchor = findViewById(R.id.menuPopupAnchor);
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuListener(item);
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
    }

    public boolean menuListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customize:
                Intent myIntent = new Intent(this, ChannelsActivity.class);
                startActivity(myIntent);
                break;
            case R.id.about:

                break;
        }
        return true;
    }

    public void clickButtonNews(View view){
        Intent myIntent = new Intent(this, NewsActivity.class);
        startActivity(myIntent);
        //disable button?
    }

    public void clickButtonCalendar(View view){
        Intent myIntent = new Intent(this, CalendarActivity.class);
        startActivity(myIntent);
        //disable button?
    }

    public void clickButtonDC(View view){
        Intent myIntent = new Intent(this, ChannelsActivity.class);
        startActivity(myIntent);
        //disable button?
    }

}
