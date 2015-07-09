package com.ilyarudyak.android.popmovies.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ilyarudyak.android.popmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This adapter receives list of Movies and use:
 * a) path to poster in call to Picasso library;
 * b) other information to show details activity;
 */
public class PicassoAdapter  extends ArrayAdapter<Movie> {

    private Context mContext;

    public PicassoAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_item_image, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);

        Picasso.with(mContext)
                .load(movie.getPosterPathAbsolute())
                .placeholder(R.raw.place_holder)
                .error(R.raw.big_problem)
                .resize(550, 775)
                .into(imageView);

        return convertView;
    }
}















