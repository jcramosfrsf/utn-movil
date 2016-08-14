package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasguti.utnmovil.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import java.text.SimpleDateFormat;

import adapter.ImgHandler;
import model.New;

public class NewFullActivity extends AppCompatActivity {

    private New newItem;
    private int layoutWidth = 480;
    private ImgHandler imageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_full);
        Intent i = getIntent();
        newItem = (New) i.getParcelableExtra("noticia");

        // Lookup view for data population
        final TextView header = (TextView) findViewById(R.id.header);
        final TextView title = (TextView) findViewById(R.id.title);
        final TextView body = (TextView) findViewById(R.id.body);

        if(!newItem.imagen.isEmpty()){
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Picasso.with(getApplicationContext()).load(newItem.imagen).into(imageView);
        }

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = df.format(newItem.fecha);
        // Populate the data into the template view using the data object
        header.setText(newItem.autor + " - " + date);
        title.setText(newItem.titulo);

        HtmlSpanner htmlSpanner = new HtmlSpanner();
        htmlSpanner.unregisterHandler("img");
        imageHandler = new ImgHandler(this, body);
        htmlSpanner.registerHandler("img", imageHandler);

        //htmlSpanner.setStripExtraWhiteSpace(true);
        Spannable spanned = htmlSpanner.fromHtml(newItem.cuerpo);
        body.setText(spanned);
        body.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        layoutWidth = findViewById(R.id.scrollView).getWidth();
        imageHandler.setMaxWidth(layoutWidth);
    }
}
