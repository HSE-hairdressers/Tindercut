package com.example.tindercut;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Adapter for nested recyclerviews inside scrolling activivty
 * The lower level of nesting
 */
public class HorizontalDataAdapter extends RecyclerView.Adapter<HorizontalDataAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private final ArrayList<String> mImageUrls;

    /**
     * Constructor
     * @param imageUrls Array of progile pics' urls
     */
    public HorizontalDataAdapter(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    /**
     * Сreates a new ViewHolder and initializes some private fields to be used by RecyclerView
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return Returns view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_horizontal, parent, false);
        return new HorizontalDataAdapter.ViewHolder(view);
    }

    /**
     * Updates the ViewHolder contents with the item at the given position and also sets up some
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String url = mImageUrls.get(position);

        Glide.with((holder).imageView.getContext()).load(url).into((holder).imageView);

    }

    /**
     * Counts items
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return mImageUrls == null ? 0 : mImageUrls.size();
    }

    /**
     * Describes an item view and metadata about its place within the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageLoaded2);

        }
    }
}
