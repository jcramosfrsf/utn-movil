package activities.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasguti.utnmovil.R;

import java.text.SimpleDateFormat;

public class NewFull extends AppCompatActivity {

    private New newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_full);
        Intent i = getIntent();
        newItem = (New) i.getParcelableExtra("noticia");

        // Lookup view for data population
        TextView header = (TextView) findViewById(R.id.header);
        TextView title = (TextView) findViewById(R.id.title);
        TextView body = (TextView) findViewById(R.id.body);

        if(newItem.imagen != "null"){
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Picasso.with(getApplicationContext()).load(newItem.imagen).into(imageView);
        }

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = df.format(newItem.fecha);
        // Populate the data into the template view using the data object
        header.setText(newItem.autor + " - " + date);
        title.setText(newItem.titulo);
        body.setText(newItem.cuerpo);

    }
}
