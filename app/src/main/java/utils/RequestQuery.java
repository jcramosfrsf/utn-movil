package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by Tomas on 17/05/2016.
 */
public class RequestQuery {
    private static RequestQuery mInstance;
    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;
    private static Context mCtx;

    private RequestQuery(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
/*
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });*/
    }

    public static synchronized RequestQuery getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQuery(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), 1024 * 5);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
/*
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }*/
}


/*
String url ="http://localhost:8888/news";

JsonArrayRequest mJsonRequest = new JsonArrayRequest(url,
            new Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    // here you can parse response and use accordingly
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // here you will receive errors and show proper message according to error type

                }
            });

 RequestQuery.getInstance(this).addToRequestQueue(mJsonRequest);
 */