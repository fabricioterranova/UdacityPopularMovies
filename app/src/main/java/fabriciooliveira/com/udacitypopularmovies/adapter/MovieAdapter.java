package fabriciooliveira.com.udacitypopularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fabriciooliveira.com.udacitypopularmovies.R;
import fabriciooliveira.com.udacitypopularmovies.UdacityPopularMoviesApplication;
import fabriciooliveira.com.udacitypopularmovies.model.Movie;

/**
 * Created by fabricio.bezerra on 19/01/2017.
 */

public class MovieAdapter extends BaseAdapter {

    private List<Movie> mMovieList;
    private LayoutInflater inflater;
    private Context mContext;
    private ImageLoader mImageLoader = UdacityPopularMoviesApplication.getInstance().getmImageLoader();

    public MovieAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public List<Movie> getmMovieList() {
        return mMovieList;
    }

    public void setmMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Movie movie = mMovieList.get(position);

        if (inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_movie_card, null);
        }

        if(mImageLoader == null){
            mImageLoader = UdacityPopularMoviesApplication.getInstance().getmImageLoader();
        }

        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.image_thumbnail);
        TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_title);


        networkImageView.setImageUrl("https://image.tmdb.org/t/p/w185" + movie.getPosterPath(), mImageLoader);
        movieTitle.setText(movie.getTitle());

        return convertView;

    }
}
