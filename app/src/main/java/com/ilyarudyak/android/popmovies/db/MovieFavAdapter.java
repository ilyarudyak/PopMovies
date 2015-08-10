package com.ilyarudyak.android.popmovies.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.ilyarudyak.android.popmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ilyarudyak on 8/8/15.
 */
public class MovieFavAdapter extends CursorAdapter {

    private Context mContext;

    public MovieFavAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        String posterPathAbsolute = cursor.getString(cursor.getColumnIndex(
                MovieContract.MovieTable.DB_POSTER_PATH_ABSOLUTE));

        Picasso.with(mContext)
                .load(posterPathAbsolute)
                .placeholder(R.raw.placeholder)
                .error(R.raw.placeholder)
                .resize(550, 775)
                .into(imageView);
    }
}















