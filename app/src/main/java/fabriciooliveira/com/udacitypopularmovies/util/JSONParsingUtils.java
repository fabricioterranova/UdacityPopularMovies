package fabriciooliveira.com.udacitypopularmovies.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fabriciooliveira.com.udacitypopularmovies.model.Movie;

/**
 * Created by fabricio.bezerra on 20/01/2017.
 */

public class JSONParsingUtils {

    public static List<Movie> jsonParser(String s) {
        List<Movie> movies = null;

        try {
            JSONObject mainObject = new JSONObject(s);
            JSONArray resultsArray = mainObject.getJSONArray("results");

            movies = new ArrayList<Movie>();

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject indexObject = resultsArray.getJSONObject(i);
                Movie indexMovie = new Movie();
                indexMovie.setBackdropPath(indexObject.getString("backdrop_path"));
                indexMovie.setId(indexObject.getInt("id"));
                indexMovie.setOriginalTitle(indexObject.getString("original_title"));
                indexMovie.setOverview(indexObject.getString("overview"));
                indexMovie.setReleaseDate(indexObject.getString("release_date"));
                indexMovie.setPosterPath(indexObject.getString("poster_path"));
                indexMovie.setPopularity(indexObject.getDouble("popularity"));
                indexMovie.setTitle(indexObject.getString("title"));
                indexMovie.setVoteAverage(indexObject.getInt("vote_average"));
                indexMovie.setVoteCount(indexObject.getInt("vote_count"));

                movies.add(indexMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

}
