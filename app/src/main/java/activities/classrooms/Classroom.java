package activities.classrooms;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.tomasguti.utnmovil.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import utils.RequestQuery;

/**
 * Created by Tomas on 10/07/2016.
 */
public class Classroom {
    public int id_carrera;
    public String nombre;
    public String comision;
    public String aula;
    public String horario;

    public Classroom(){

    }

    public void loadDataFromServer(final Context context, final Callback callback, final String fecha_inicio, final int carrera, final int nivel, final int materia, final int comisiones){

        String url = context.getResources().getString(R.string.utn_server_url)+"/getDistribucion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Parse
                        //Log.d("Response", response);
                        Document doc = Jsoup.parse(response);
                        Elements tableRow = doc.getElementsByTag("td");
                        ArrayList<String> list = new ArrayList<>();
                        for (Element column : tableRow) {
                            String attr = column.attr("width");
                            if(attr.isEmpty()){
                                String text = column.text();
                                list.add(text);
                            }
                        }
                        if(list.size() > 3){
                            horario = list.get(1);
                            aula = list.get(3);
                        }else{
                            horario = context.getResources().getString(R.string.subject_no_info);
                            aula = "";
                        }
                        callback.onSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "Error Materia "+nombre);
                horario = context.getResources().getString(R.string.subject_error);
                aula = "";
                callback.onError();
            }
        }){
            String mRequestBody = "fecha_inicio="+fecha_inicio+"&carrera="+carrera+"&nivel="+nivel+"&materia="+materia+"&comisiones="+comisiones;
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
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

        RequestQuery.getInstance(context).addToRequestQueue(stringRequest);
    }
}
