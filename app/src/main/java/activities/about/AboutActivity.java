package activities.about;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textView = (TextView) findViewById(R.id.textView);
        loadLicensesText(textView);
    }

    private void loadLicensesText(TextView textView){
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.licenses);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            textView.setText(new String(b));
        } catch (Exception e) {
            // e.printStackTrace();
            textView.setText("Error: can't show help.");
        }
    }
}
