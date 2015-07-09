package com.ilyarudyak.android.popmovies.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ilyarudyak.android.popmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter provides information from network API call.
 * We provide to the adapter a list of Movie objects. For
 * our GridView we need only list of absolute poster paths. We
 * extract them and transfer to Picasso call. Other information
 * we use to create Detail activity.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mMoviesList;

    public ImageAdapter(Context context, List<Movie> moviesList) {
        mContext = context;
        mMoviesList = moviesList;
    }

    public void clear() {
        mMoviesList = new ArrayList<>();
    }

    public void addAll(List<Movie> list) {
        mMoviesList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMoviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        ImageView imageView;
        // check to see if we have a view
        if (convertView == null) {
            // no view - so create a new one
            imageView = new ImageView(mContext);
        } else {
            // use the recycled view object
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mMoviesList.get(position).getPosterPathAbsolute())
                .placeholder(R.raw.place_holder)
                .error(R.raw.big_problem)
                .resize(550, 775)
                .into(imageView);
        return imageView;
    }
}
