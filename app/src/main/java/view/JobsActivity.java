package view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

import model.utils.RequestQuery;

public class JobsActivity extends AppCompatActivity {

    TextView htmlContent;
    ProgressBar progressBarRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        progressBarRequest = (ProgressBar) findViewById(R.id.progressBarRequest);
        htmlContent = (TextView) findViewById(R.id.htmlContent);
        loadDataFromServer();
    }

    public void loadDataFromServer(){

        String url = getResources().getString(R.string.utn_server_url)+"/graduados/busquedas-laborales.html";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Parse
                        Document doc = Jsoup.parse(response);
                        Element body = doc.getElementById("body");
                        body.select("script").remove();
                        Elements links = body.select("a");
                        for(Element link : links){
                            if(link.attr("href").startsWith("mailto:")){
                                if(link.toString().contains("estudiantes")){
                                    link.text("estudiantes@frsf.utn.edu.ar");
                                    link.attr("href", "mailto:estudiantes@frsf.utn.edu.ar");
                                }else{
                                    link.text("graduados@frsf.utn.edu.ar");
                                    link.attr("href", "mailto:graduados@frsf.utn.edu.ar");
                                }
                            }
                            if(link.attr("href").startsWith("/")){
                                link.attr("href", "http://www.frsf.utn.edu.ar"+link.attr("href"));
                            }
                        }

                        Element mcePaste = body.getElementById("_mcePaste");
                        if(mcePaste != null){
                            mcePaste.remove();
                        }

                        HtmlSpanner htmlSpanner = new HtmlSpanner();
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
