package activities.news;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasguti.utnmovil.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.SpanStack;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

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
        htmlSpanner.registerHandler("img", new ImgHandler(body));
        htmlSpanner.setStripExtraWhiteSpace(true);
        Spannable spanned = htmlSpanner.fromHtml(newItem.cuerpo);
        body.setText(spanned);
        body.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ImgHandler extends TagNodeHandler {

        private TextView textView;
        public ImgHandler(TextView tv){
            textView = tv;
        }

        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder,
                                  int start, int end, SpanStack stack) {
            String src = node.getAttributeByName("src");

            builder.append("\uFFFC");
            LevelListDrawable drawable = new LevelListDrawable();
            Drawable empty = getResources().getDrawable(R.drawable.abc_btn_check_material);;
            drawable.addLevel(0, 0, empty);
            drawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
            new ImageGetterAsyncTask(getApplicationContext(), src, drawable).execute(textView);
            stack.pushSpan( new ImageSpan(drawable), start, builder.length() );
        }
    }

    class ImageGetterAsyncTask extends AsyncTask<TextView, Void, Bitmap> {


        private LevelListDrawable levelListDrawable;
        private Context context;
        private String source;
        private TextView t;

        public ImageGetterAsyncTask(Context context, String source, LevelListDrawable levelListDrawable) {
            this.context = context;
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextView... params) {
            t = params[0];
            try {
                Log.d("NewFull", "Downloading the image from: " + source);
                return Picasso.with(context).load(source).get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                levelListDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }
}
