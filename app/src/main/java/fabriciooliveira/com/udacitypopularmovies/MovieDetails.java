package fabriciooliveira.com.udacitypopularmovies;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fabriciooliveira.com.udacitypopularmovies.model.Movie;

/**
 * Created by fabricio.bezerra on 20/01/2017.
 */

public class MovieDetails extends AppCompatActivity {

    private Movie mMovie;
    private TextView mTitle, mPlot, mReleaseDate, mPopularity;
    private ImageLoader mImageLoader;
    private NetworkImageView mMovieThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mImageLoader = UdacityPopularMoviesApplication.getInstance().getmImageLoader();

        if (savedInstanceState == null) {
            Bundle bundle = this.getIntent().getExtras();
            mMovie = bundle.getParcelable(getString(R.string.movie));
        } else {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeComponents();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.movie), mMovie);
    }

    private void initializeComponents() {
        mTitle = (TextView) findViewById(R.id.title_movie);
        mPlot = (TextView) findViewById(R.id.description_movie);
        mMovieThumbnail = (NetworkImageView) findViewById(R.id.image_thumbnail);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mPopularity = (TextView) findViewById(R.id.popularity);

        if(mImageLoader == null){
            mImageLoader = UdacityPopularMoviesApplication.getInstance().getmImageLoader();
        }

        mTitle.setText(mMovie.getTitle());
        mPlot.setText(mMovie.getOverview());
        mPopularity.setText(String.valueOf(mMovie.getPopularity()));

        mMovieThumbnail.setImageUrl("https://image.tmdb.org/t/p/w185" + mMovie.getPosterPath(), mImageLoader);

        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date = dateFormat.parse(mMovie.getReleaseDate());
        }catch(ParseException e){
            e.printStackTrace();
        }

        if(date != null){
            mReleaseDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
        }else{
            mReleaseDate.setText(getString(R.string.NA));
        }

    }

}
