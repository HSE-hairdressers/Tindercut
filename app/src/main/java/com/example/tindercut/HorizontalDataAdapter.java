package com.example.tindercut;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HorizontalDataAdapter extends RecyclerView.Adapter<HorizontalDataAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private final ArrayList<String> mImageUrls;

    public HorizontalDataAdapter(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_horizontal, parent, false);
        return new HorizontalDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String url = mImageUrls.get(position);

        Glide.with((holder).imageView.getContext()).load(url).into((holder).imageView);

    }

    @Override
    public int getItemCount() {
        return mImageUrls == null ? 0 : mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageLoaded2);
        }
    }
}
