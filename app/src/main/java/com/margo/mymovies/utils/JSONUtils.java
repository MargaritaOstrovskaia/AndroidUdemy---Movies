package com.margo.mymovies.utils;

import com.margo.mymovies.data.Movie;
import com.margo.mymovies.data.Review;
import com.margo.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    private static final String KEY_RESULTS = "results";

    // for reviews
    private static final String KEY_REVIEW_AUTHOR = "author";
    private static final String KEY_REVIEW_CONTENT = "content";

    // for videos
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String KEY_VIDEO_KEY = "key";
    private static final String KEY_VIDEO_NAME = "name";

    // all information about movie
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATA= "release_date";

    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String BIG_POSTER_SIZE = "w780";

    public static ArrayList<Trailer> getTrailersFromJSON (JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();

        if(jsonObject == null)
            return result;

        try {
            JSONArray array = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);

                String key = BASE_YOUTUBE_URL + object.getString(KEY_VIDEO_KEY);
                String name = object.getString(KEY_VIDEO_NAME);

                Trailer trailer = new Trailer(key, name);
                result.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<Review> getReviewsFromJSON (JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();

        if(jsonObject == null)
            return result;

        try {
            JSONArray array = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);

                String author = object.getString(KEY_REVIEW_AUTHOR);
                String content = object.getString(KEY_REVIEW_CONTENT);

                Review review = new Review(author, content);
                result.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject){
        ArrayList<Movie> result = new ArrayList<>();

        if(jsonObject == null)
            return result;

        try {
            JSONArray array = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);

                int id = object.getInt(KEY_ID);
                int voteCount = object.getInt(KEY_VOTE_COUNT);
                String title = object.getString(KEY_TITLE);
                String originalTitle = object.getString(KEY_ORIGINAL_TITLE);
                String overview = object.getString(KEY_OVERVIEW);

                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + object.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + object.getString(KEY_POSTER_PATH);

                String backdropPath = object.getString(KEY_BACKDROP_PATH);
                double voteAverage = object.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = object.getString(KEY_RELEASE_DATA);

                Movie movie = new Movie(id, voteCount, title, originalTitle, overview,
                        posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
