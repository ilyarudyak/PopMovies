package com.ilyarudyak.android.popmovies.fav;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilyarudyak.android.popmovies.DetailFragment;
import com.ilyarudyak.android.popmovies.utils.DbUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavDetailFragment extends DetailFragment {

    public static final String TAG = FavDetailFragment.class.getSimpleName();


    public FavDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new FetchBundleTask().execute();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    // ------------------ fetch mBundle ------------------

    public class FetchBundleTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Uri movieUri = getActivity().getIntent().getParcelableExtra(
                    FavPosterFragment.DETAIL_URI);

            mBundle = DbUtils.getBundleFromUri(getActivity(), movieUri);

//            Log.d(TAG, mBundle.toString());
            return null;
        }
    }
}
