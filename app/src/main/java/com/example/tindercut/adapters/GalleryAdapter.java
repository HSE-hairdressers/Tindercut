package com.example.tindercut.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tindercut.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final ArrayList<String> images;
    private final Context context;
    private final LayoutInflater inflater;

    /**
     * Конструктор
     */
    public GalleryAdapter(Context context, ArrayList<String> images) {
        this.images = images;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Сreates a new ViewHolder and initializes some private fields to be used by RecyclerView
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return Returns view
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("DEV", "ok");
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loaded_imageline, parent, false);
            return new GalleryAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new GalleryAdapter.LoadingViewHolder(view);
        }
    }

    /**
     * Updates the ViewHolder contents with the item at the given position and also sets up some
     * private fields to be used by RecyclerView.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GalleryAdapter.ItemViewHolder) {

            String [] urs = new String[3];
            for(int j = 0; j < 3; ++j){

                if ((position * 3) + (j+1) >= images.size()){
                    break;
                }

                urs[j] = images.get( (position * 3) + (j+1));
                Log.v("DEV", urs[j]);

            }

            if (urs[0] != null){
                Glide.with(((ItemViewHolder) holder).image1.getContext()).load(urs[0]).into(((ItemViewHolder) holder).image1);
            }

            if (urs[1] != null){
                Glide.with(((ItemViewHolder) holder).image2.getContext()).load(urs[1]).into(((ItemViewHolder) holder).image2);
            }

            if (urs[2] != null){
                Glide.with(((ItemViewHolder) holder).image3.getContext()).load(urs[2]).into(((ItemViewHolder) holder).image3);
            }

        } else {
            showLoadingView((GalleryAdapter.LoadingViewHolder) holder, position);
        }

    }

    /**
     * Returns type of required position
     * @param position position to query
     * @return type of position
     */
    @Override
    public int getItemViewType(int position) {
        return images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /**
     * Counts items
     * @return number of items
     */
    @Override
    public int getItemCount() {

        return images == null ? 0 : (int) Math.ceil((double) images.size() / 3);
    }

    /**
     * Empty func for actions with loading animation
     * @param viewHolder
     * @param position
     */
    private void showLoadingView(GalleryAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    /**
     * Describes an item view and metadata about its place within the RecyclerView
     */
    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image1;
        ImageView image2;
        ImageView image3;

        /**
         * Implements listener for opening hairdresser profile
         * @param view required View
         */
        public ItemViewHolder(View view) {
            super(view);
            image1 = view.findViewById(R.id.profile_work_1);
            image2 = view.findViewById(R.id.profile_work_2);
            image3 = view.findViewById(R.id.profile_work_3);
        }
    }

    /**
     * Describes an loading item view and metadata about its place within the RecyclerView
     */
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
