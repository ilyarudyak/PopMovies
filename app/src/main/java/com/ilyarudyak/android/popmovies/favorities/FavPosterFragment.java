package com.ilyarudyak.android.popmovies.favorities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ilyarudyak.android.popmovies.R;
import com.ilyarudyak.android.popmovies.db.MovieFavAdapter;
import com.ilyarudyak.android.popmovies.db.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavPosterFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int FAV_LOADER = 0;

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

        return rootView;
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
