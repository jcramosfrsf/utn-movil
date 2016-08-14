package view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.tomasguti.utnmovil.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

import adapter.ImgHandler;
import model.utils.RequestQuery;

public class LecturesActivity extends AppCompatActivity {

    TextView htmlContent;
    ProgressBar progressBarRequest;
    private int layoutWidth = 480;
    private ImgHandler imageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        progressBarRequest = (ProgressBar) findViewById(R.id.progressBarRequest);
        htmlContent = (TextView) findViewById(R.id.htmlContent);
        loadDataFromServer();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        layoutWidth = findViewById(R.id.scroll).getWidth();
    }

    public void loadDataFromServer(){

        String url = getResources().getString(R.string.utn_server_url)+"/cursos";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Parse
                        Document doc = Jsoup.parse(response);
                        Element body = doc.getElementById("body");
                        body.select("script").remove();
                        Elements links = body.select("span");
                        for(Element link : links){
                            if(link.attr("id").startsWith("cloak")){
                                link.tagName("a");
                                link.text("infocursos@frsf.utn.edu.ar");
                                link.attr("href", "mailto:infocursos@frsf.utn.edu.ar");
                            }
                        }
                        Elements imgs = body.select("img");
                        for(Element img : imgs){
                            if(img.attr("src").startsWith("/")){
                                img.attr("src", "http://www.frsf.utn.edu.ar"+img.attr("src"));
                            }
                        }

                        HtmlSpanner htmlSpanner = new HtmlSpanner();
                        htmlSpanner.unregisterHandler("img");
                        imageHandler = new ImgHandler(getApplicationContext(), htmlContent);
                        imageHandler.setMaxWidth(layoutWidth);
                        htmlSpanner.registerHandler("img", imageHandler);
                        Spannable spanned = htmlSpanner.fromHtml( body.toString());
                        htmlContent.setText(spanned);
                        htmlContent.setMovementMethod(LinkMovementMethod.getInstance());
                        progressBarRequest.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
                progressBarRequest.setVisibility(View.GONE);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        RequestQuery.getInstance(this).addToRequestQueue(stringRequest);
    }
}
