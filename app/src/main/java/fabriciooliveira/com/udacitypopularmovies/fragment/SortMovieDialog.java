package fabriciooliveira.com.udacitypopularmovies.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import fabriciooliveira.com.udacitypopularmovies.R;
import fabriciooliveira.com.udacitypopularmovies.callback.IUpdateSortingMovieCallback;

/**
 * Created by fabricio.bezerra on 23/01/2017.
 */

public class SortMovieDialog extends DialogFragment {

    private RadioButton mRadioButtonMostPopular, mRadioButtonHighestRated;
    private TextView mCancel, mConfim;

    IUpdateSortingMovieCallback mListener;

    public SortMovieDialog() {

    }

    public static SortMovieDialog newInstance() {
        SortMovieDialog frag = new SortMovieDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sort_movie_dialog, container);

        mRadioButtonMostPopular = (RadioButton) view.findViewById(R.id.most_popular);
        mRadioButtonHighestRated = (RadioButton) view.findViewById(R.id.highest_rated);

        mCancel = (TextView) view.findViewById(R.id.cancel);
        mConfim = (TextView) view.findViewById(R.id.confirm);

        if (getString(R.string.highest_rated).equals(getUserChoiceSortMovie(getActivity()))) {
            mRadioButtonHighestRated.setChecked(true);
        } else {
            mRadioButtonMostPopular.setChecked(true);
        }

        mConfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioButtonMostPopular.isChecked()) {
                    setUserChoiceSortMovie(getActivity(), mRadioButtonMostPopular.getTag().toString());
                    mListener.updateMovieList(mRadioButtonMostPopular.getTag());
                } else {
                    setUserChoiceSortMovie(getActivity(), mRadioButtonHighestRated.getTag().toString());
                    mListener.updateMovieList(mRadioButtonHighestRated.getTag());
                }

                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IUpdateSortingMovieCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement IUpdateSortingMovieCallback");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static void setUserChoiceSortMovie(Context context, String choice) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.status_user_choice), choice);

        editor.apply();
    }

    public static String getUserChoiceSortMovie(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.status_user_choice), "");
    }

}
