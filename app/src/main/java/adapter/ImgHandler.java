package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasguti.utnmovil.R;

import net.nightwhistler.htmlspanner.SpanStack;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

/**
 * Created by Tomas on 14/08/2016.
 */
public class ImgHandler extends TagNodeHandler {

    private int maxWidth;
    private TextView textView;
    private Context context;

    public ImgHandler(Context ctxt, TextView tv){
        textView = tv;
        context = ctxt;
        maxWidth = tv.getWidth();
        if(maxWidth == 0){
            maxWidth = 480;
        }
    }

    public void setMaxWidth(int width){
        maxWidth = width;
    }

    @Override
    public void handleTagNode(TagNode node, SpannableStringBuilder builder,
                              int start, int end, SpanStack stack) {
        String src = node.getAttributeByName("src");

        builder.append("\uFFFC");
        LevelListDrawable drawable = new LevelListDrawable();
        Drawable empty = context.getResources().getDrawable(R.drawable.abc_btn_check_material);
        drawable.addLevel(0, 0, empty);
        drawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new ImageGetterAsyncTask(src, drawable).execute(textView);
        stack.pushSpan( new ImageSpan(drawable), start, builder.length() );
    }

    class ImageGetterAsyncTask extends AsyncTask<TextView, Void, Bitmap> {

        private LevelListDrawable levelListDrawable;
        private String source;
        private TextView t;

        public ImageGetterAsyncTask(String source, LevelListDrawable levelListDrawable) {
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextView... params) {
            t = params[0];
            try {
                Log.d("NewFullActivity", "Downloading the image from: " + source+" "+maxWidth);
                return Picasso.with(context).load(source).get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                float ratio = (float) maxWidth/bitmap.getWidth();
                if(ratio > 1.0f){
                    ratio = 1.0f;
                }
                int width = Math.round(bitmap.getWidth()*ratio);
                int height = Math.round(bitmap.getHeight()*ratio);
                levelListDrawable.setBounds(0, 0, width, height);
                levelListDrawable.setLevel(1);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }
}
