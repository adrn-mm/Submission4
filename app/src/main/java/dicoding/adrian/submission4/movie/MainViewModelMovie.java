package dicoding.adrian.submission4.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainViewModelMovie extends ViewModel {

    private static final String API_KEY = "f733887094fe514518e8087c86f26c59";
    private MutableLiveData<ArrayList<MovieItems>> listMovies = new MutableLiveData<>();
    private String lang;

    void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItems> listItems = new ArrayList<>();

        String locale = Locale.getDefault().getDisplayLanguage();
        if (locale.contains("English")) {
            lang = "en-US";
        } else if (locale.contains("Indonesia")) {
            lang = "id";
        }

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=" + lang;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        listItems.add(movieItems);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    void searchMovie(final String movies) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItems> listItems = new ArrayList<>();

        String locale = Locale.getDefault().getDisplayLanguage();
        if (locale.contains("English")) {
            lang = "en-US";
        } else if (locale.contains("Indonesia")) {
            lang = "id";
        }

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY
                + "&language=" + lang
                + "&query=" + movies;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        listItems.add(movieItems);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    LiveData<ArrayList<MovieItems>> getMovies() {
        return listMovies;
    }
}
