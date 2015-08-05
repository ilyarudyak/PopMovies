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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_item_image, parent, false);

            // cache view fields into the holder
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(holder);
        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        ImageView imageView = holder.imageView;
        // to eliminate horizontal spacing between images on a tablet
        imageView.setAdjustViewBounds(true);

        Picasso.with(mContext)
                .load(movie.getPosterPathAbsolute())
                .placeholder(R.raw.place_holder)
                .error(R.raw.big_problem)
                .resize(550, 775)
                .into(imageView);

        return convertView;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public ImageView imageView;
    }
}















