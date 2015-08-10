package com.ilyarudyak.android.popmovies.fav;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilyarudyak.android.popmovies.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavDetailFragment extends Fragment {

    public FavDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_detail, container, false);
    }
}
