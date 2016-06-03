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
                    "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                    "author:'AUTHOR'"+
                "}," +
                "{" +
                    "title:'Título Noticia 4'," +
                    "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                    "author:'AUTHOR'"+
                "}," +
                "{" +
                "title:'Título Noticia 5'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'AUTHOR'"+
                "}," +
                "{" +
                "title:'Título Noticia 6'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'AUTHOR'"+
                "}," +
                "{" +
                "title:'Título Noticia 7'," +
                "body:'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'," +
                "author:'AUTHOR'"+
                "}" +
            "]";

    public JSONNews() throws JSONException{
        super(json);
    }
}
