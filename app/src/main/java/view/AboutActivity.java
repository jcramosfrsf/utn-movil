package view;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomasguti.utnmovil.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import java.io.InputStream;

import adapter.ImgHandler;

public class AboutActivity extends AppCompatActivity {

    private TextView textView;
    private HtmlSpanner htmlSpanner;
    private ImageView logotics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(listener);

        logotics = (ImageView) findViewById(R.id.logotics);

        textView = (TextView) findViewById(R.id.textView);
        htmlSpanner = new HtmlSpanner();
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        loadRawText(R.raw.credits);
    }

    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch(tab.getPosition()){
                case 0:
                    logotics.setVisibility(View.VISIBLE);
                    loadRawText(R.raw.credits);
                    break;
                case 1:
                    logotics.setVisibility(View.GONE);
                    loadRawText(R.raw.licenses);
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    private void loadRawText(int id){
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(id);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);

            Spannable spanned = htmlSpanner.fromHtml(new String(b));
            textView.setText(spanned);
        } catch (Exception e) {
            // e.printStackTrace();
            textView.setText("Error: can't show.");
        }
    }
}
