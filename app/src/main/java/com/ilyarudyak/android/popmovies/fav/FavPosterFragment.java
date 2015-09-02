package com.ilyarudyak.android.popmovies.fav;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.ilyarudyak.android.popmovies.R;
import com.ilyarudyak.android.popmovies.db.MovieContract;
import com.ilyarudyak.android.popmovies.db.MovieFavAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavPosterFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = FavPosterFragment.class.getSimpleName();

    private static final int FAV_LOADER = 0;
    public static final String DETAIL_URI = "detail_uri";

    private MovieFavAdapter mMovieFavAdapter;

    public FavPosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        mMovieFavAdapter = new MovieFavAdapter(getActivity(), null, 0);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(mMovieFavAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(buildDetailIntent(id));
            }
        });

        // remove progress indicator
        ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.progress_spinner);
        spinner.setVisibility(View.GONE);

        return rootView;
    }

    private Intent buildDetailIntent(long id) {
        Intent i = new Intent(getActivity(), FavDetailActivity.class);
        Uri detailUri = MovieContract.MovieTable.buildMovieUri(id);
        Log.d(TAG, detailUri.toString());
        i.putExtra(DETAIL_URI, detailUri);
        return i;
    }

    // ------------------ loader ------------------

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAV_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieFavAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieFavAdapter.swapCursor(null);
    }
}
