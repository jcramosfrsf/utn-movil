package utils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Tomás on 29/05/2016.
 */
public class JSONStubs{

    private static String channels =
            "["+
                    "{" +
                    "_id:'global'," +
                    "nombre:'Institucional'," +
                    "desc:'Noticias sobre asuntos pertinentes a toda la comunidad educativa.'" +
                    "}," +
                    "{" +
                    "_id:'sistemas'," +
                    "nombre:'Departamento Sistemas'," +
                    "desc:'Canal del departamento sistemas.'" +
                    "}" +
            "]";

    private static String news =
            "["+
                "{" +
                    "title:'Título Noticia 1'," +
                    "body:'Cuerpo de la noticia'," +
                    "author:'global'"+
                "}," +
                "{" +
                    "title:'Título Noticia 2'," +
                    "body:'Cuerpo de la segunda noticia'," +
                    "author:'sistemas'"+
                "}," +
                "{" +
                    "title:'Título Noticia 3'," +
                    "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                    "author:'autor'"+
                "}," +
                "{" +
                    "title:'Título Noticia 4'," +
                    "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                    "author:'autor'"+
                "}," +
                "{" +
                "title:'Título Noticia 5'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'autor'"+
                "}," +
                "{" +
                "title:'Título Noticia 6'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'autor'"+
                "}," +
                "{" +
                "title:'Título Noticia 7'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'autor'"+
                "}" +
            "]";

    public static JSONArray getNews(){
        JSONArray array = null;
        try {
            array = new JSONArray(news);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public static JSONArray getChannels(){
        JSONArray array = null;
        try {
            array = new JSONArray(channels);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
}
