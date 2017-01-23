package fabriciooliveira.com.udacitypopularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import fabriciooliveira.com.udacitypopularmovies.adapter.MovieAdapter;
import fabriciooliveira.com.udacitypopularmovies.api.APIEndpoints;
import fabriciooliveira.com.udacitypopularmovies.callback.IUpdateSortingMovieCallback;
import fabriciooliveira.com.udacitypopularmovies.fragment.SortMovieDialog;
import fabriciooliveira.com.udacitypopularmovies.model.Movie;
import fabriciooliveira.com.udacitypopularmovies.util.JSONParsingUtils;

public class MainActivity extends AppCompatActivity implements GridView.OnItemClickListener, IUpdateSortingMovieCallback{

    private ProgressBar mProgressBar;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList = new ArrayList<Movie>();
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        initializeComponents();

        if (savedInstanceState != null) {
            mMovieList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_state));
            mMovieAdapter.setmMovieList(mMovieList);
            mGridView.setAdapter(mMovieAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            requestMovieList("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + APIEndpoints.API_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(getString(R.string.movie_state), (ArrayList<Movie>)mMovieList);
    }

    private void initializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_title));

        mRequestQueue = UdacityPopularMoviesApplication.getInstance().getmRequestQueue();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mGridView = (GridView) findViewById(R.id.gridview_movie);
        mGridView.setOnItemClickListener(this);

        mMovieAdapter = new MovieAdapter(MainActivity.this);
    }


    private void requestMovieList(String url){
        clearMovieList(mMovieList);
        mProgressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
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
            showMovieSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMovieSortDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SortMovieDialog sortMovieDialog = SortMovieDialog.newInstance();
        sortMovieDialog.show(fm, getString(R.string.fragment_sort_movie));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Movie movie = mMovieList.get(i);

        if (movie != null) {
            Intent intent=new Intent(this, MovieDetails.class);
            intent.putExtra(getString(R.string.movie), movie);

            startActivity(intent);
        }
    }

    private void clearMovieList(List<Movie> list) {
        if (list != null) {
            list.clear();
        }
    }

    @Override
    public void updateMovieList(Object movieSorter) {
        String url = null;

        if (getString(R.string.most_popular).equals(movieSorter)) {
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + APIEndpoints.API_KEY;
        } else {
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=" + APIEndpoints.API_KEY;
        }

        requestMovieList(url);
    }


    class Sucesso implements Response.Listener<String> {

        public void onResponse(String response) {
            if (!TextUtils.isEmpty(response.toString())) {
                mMovieList = JSONParsingUtils.jsonParser(response.toString());

                mMovieAdapter.setmMovieList(mMovieList);
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

}
