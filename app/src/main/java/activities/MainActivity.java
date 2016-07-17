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
import android.view.View;

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
