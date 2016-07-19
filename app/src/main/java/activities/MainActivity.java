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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import activities.about.AboutActivity;
import activities.calendar.CalendarActivity;
import activities.classrooms.ClassroomsActivity;
import activities.news.NewsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onResume(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int noticias = sp.getInt("noticias", 0);
        int eventos = sp.getInt("eventos", 0);
        int mensajes = sp.getInt("mensajes", 0);

        ImageView noticiasNotification = (ImageView) findViewById(R.id.noticiasNotification);
        TextView noticiasNotificationText = (TextView) findViewById(R.id.noticiasNotificationText);

        ImageView eventosNotification = (ImageView) findViewById(R.id.eventosNotification);
        TextView eventosNotificationText = (TextView) findViewById(R.id.eventosNotificationText);

        if(noticias > 0){
            noticiasNotification.setVisibility(View.VISIBLE);
            noticiasNotificationText.setVisibility(View.VISIBLE);
            noticiasNotificationText.setText(""+noticias);
        }else{
            noticiasNotification.setVisibility(View.GONE);
            noticiasNotificationText.setVisibility(View.GONE);
        }

        if(eventos > 0){
            eventosNotification.setVisibility(View.VISIBLE);
            eventosNotificationText.setVisibility(View.VISIBLE);
            noticiasNotificationText.setText(""+eventos);
        }else{
            eventosNotification.setVisibility(View.GONE);
            eventosNotificationText.setVisibility(View.GONE);
        }

        Log.d("MainActivity", "Noticias: " + noticias + " Eventos: " + eventos +" Mensajes: "+ mensajes);
        super.onResume();
    }

    public void clickButtonNews(View view){
        Intent myIntent = new Intent(this, NewsActivity.class);
        startActivity(myIntent);
    }

    public void clickButtonCalendar(View view){
        Intent myIntent = new Intent(this, CalendarActivity.class);
        startActivity(myIntent);
    }

    public void clickButtonDC(View view){
        Intent myIntent = new Intent(this, ClassroomsActivity.class);
        startActivity(myIntent);
    }

    public void clickButtonAbout(View view){
        Intent myIntent = new Intent(this, AboutActivity.class);
        startActivity(myIntent);
    }

}
