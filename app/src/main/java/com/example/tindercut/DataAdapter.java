package com.example.tindercut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final LayoutInflater inflater;
    private final ArrayList<String> Images;
    private final ArrayList<String> hairdressers;

    private Context context;

    public DataAdapter(Context context, ArrayList<String> images, ArrayList<String> hairdressers) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.Images = images;
        this.hairdressers = hairdressers;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loaded_hairdresser, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            String url = Images.get(position);
            System.out.println(position);
            System.out.println(url);
            //System.out.println(((ItemViewHolder) holder).imageView.toString());
            Glide.with(((ItemViewHolder) holder).imageView.getContext()).load(url).into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).textView.setText(hairdressers.get(position));
        }
        else {
            showLoadingView((LoadingViewHolder)holder, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return Images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {

        return hairdressers == null ? 0 : hairdressers.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ItemViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.textLoaded);
            imageView = view.findViewById(R.id.imageLoaded);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontalRecycler);
        }
    }

    private void horizontalView(HorizontalViewHolder holder) {
        HorizontalDataAdapter adapter = new HorizontalDataAdapter(Images);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }



}


