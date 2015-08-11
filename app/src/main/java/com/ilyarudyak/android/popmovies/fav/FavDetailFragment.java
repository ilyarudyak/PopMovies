package com.ilyarudyak.android.popmovies.fav;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ilyarudyak.android.popmovies.DetailFragment;
import com.ilyarudyak.android.popmovies.utils.FavDbUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavDetailFragment extends DetailFragment {

    public static final String TAG = FavDetailFragment.class.getSimpleName();

    public FavDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchBundleTask().execute();
    }

    // ------------------ fetch mBundle ------------------

    public class FetchBundleTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Uri movieUri = getActivity().getIntent().getParcelableExtra(
                    FavPosterFragment.DETAIL_URI);

            mBundle = FavDbUtils.getBundleFromUri(getActivity(), movieUri);

//            Log.d(TAG, mBundle.toString());
            return null;
        }
    }
}
