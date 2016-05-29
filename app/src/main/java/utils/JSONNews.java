package utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Tomás on 29/05/2016.
 */
public class JSONNews extends JSONArray{

    private static String json =
            "["+
                "{" +
                    "title:'Título Noticia 1'," +
                    "body:'Cuerpo de la noticia'," +
                    "author:'GLOBAL'"+
                "}," +
                "{" +
                    "title:'Título Noticia 2'," +
                    "body:'Cuerpo de la segunda noticia'," +
                    "author:'SISTEMAS'"+
                "}," +
                "{" +
                    "title:'Título Noticia 3'," +
                    "body:'Cuerpo de la tercera noticia'," +
                    "author:'ASD'"+
                "}" +
            "]";

    public JSONNews() throws JSONException{
        super(json);
    }
}
