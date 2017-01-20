package fabriciooliveira.com.udacitypopularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import fabriciooliveira.com.udacitypopularmovies.adapter.MovieAdapter;
import fabriciooliveira.com.udacitypopularmovies.api.APIEndpoints;
import fabriciooliveira.com.udacitypopularmovies.model.Movie;
import fabriciooliveira.com.udacitypopularmovies.util.JSONParsingUtils;

public class MainActivity extends AppCompatActivity implements GridView.OnItemClickListener{

    private ProgressBar mProgressBar;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList = new ArrayList<Movie>();
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    private void initializeComponents() {
        mRequestQueue = UdacityPopularMoviesApplication.getInstance().getmRequestQueue();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mGridView = (GridView) findViewById(R.id.gridview_movie);
        mGridView.setOnItemClickListener(this);

        mMovieAdapter = new MovieAdapter(MainActivity.this);

        requestMovieList();
    }


    private void requestMovieList(){
        mMovieList.clear();
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + APIEndpoints.API_KEY,
                new Sucesso(),
                new Erro()){
        };

        mRequestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Intent optionIntent = new Intent(this, OrganizationPreferenceActivity.class);
//            startActivity(optionIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "gentleman", Toast.LENGTH_SHORT).show();
    }

    class Sucesso implements Response.Listener<String> {

        public void onResponse(String response) {
            if (!TextUtils.isEmpty(response.toString())) {
                List<Movie> mListMovie = JSONParsingUtils.jsonParser(response.toString());

                mMovieAdapter.setmMovieList(mListMovie);
                mGridView.setAdapter(mMovieAdapter);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    class Erro implements Response.ErrorListener {

        public void onResponse(String response) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Movie movie = mMovieList.get(position);
//
//        if (movie != null) {
//            Intent intent = new Intent(MainActivity.this, MovieDetails.class);
//            //intent.putExtra(Constants.CATEGORY_ID, category.getId());
//            startActivity(intent);
//        }
//        //MessageUtil.addMsg(getActivity(), category.getName());
//    }

}
