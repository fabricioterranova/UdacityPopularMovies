package fabriciooliveira.com.udacitypopularmovies;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import fabriciooliveira.com.udacitypopularmovies.util.BitMapCache;

/**
 * Created by fabricio.bezerra on 19/01/2017.
 */

public class UdacityPopularMoviesApplication extends Application {

    private static final String TAG = UdacityPopularMoviesApplication.class.getSimpleName();

    private static UdacityPopularMoviesApplication mAppController;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;


    @Override
    public void onCreate() {
        super.onCreate();

        mAppController = this;
    }

    public static synchronized UdacityPopularMoviesApplication getInstance() {
        return mAppController;
    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        getmRequestQueue();

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new BitMapCache());
        }
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getmRequestQueue().add(request);
    }

    public void cancelarRequestPending(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getContext() {
        return mAppController;
    }
}
