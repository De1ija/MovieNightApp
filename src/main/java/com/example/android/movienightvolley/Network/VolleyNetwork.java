package com.example.android.movienightvolley.Network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.movienightvolley.Data.Movie;

import org.json.JSONObject;


public class VolleyNetwork {

    private static final String TAG = "VolleyNetwork";
    private static final String API_KEY = "----------API KEY GOES HERE---------";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";

    private static VolleyNetwork mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyNetwork(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyNetwork getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyNetwork(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /*
    * -------------Network methods-------------
    */

    public void fetchMovieData(String orderBy, String pageNumber, final VolleyCallback callback){
        for (int i=1; i<Integer.parseInt(pageNumber)+1; i++) {
            String movieUrl = createUrl(orderBy, Integer.toString(i));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccessResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mRequestQueue.add(request);
        }
    }

    public void fetchMovieRewiew(Movie movie, final VolleyCallback callback){
        String movieUrl = createMovieUrl(String.valueOf(movie.getMovieId()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void fetchMovieTrailer(Movie movie, final VolleyCallback callback){
        String movieTrailerUrl = createMovieTrailerUrl(String.valueOf(movie.getMovieId()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieTrailerUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    /*
    * ------------Helper methods for creating URL-------------
    */
    private String createUrl(String orderBy, String pageNumber){
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon().appendEncodedPath(orderBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.appendQueryParameter("page", pageNumber);
        return uriBuilder.toString();
    }

    private String createMovieUrl(String movieId) {
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon().appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", API_KEY);
        return uriBuilder.toString();
    }

    private String createMovieTrailerUrl(String movieId) {
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon().appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", API_KEY);
        return uriBuilder.toString();
    }
}
