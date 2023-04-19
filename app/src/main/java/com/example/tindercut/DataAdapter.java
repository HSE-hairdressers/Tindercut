package com.example.tindercut;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final LayoutInflater inflater;
    private final ArrayList<ArrayList<String>> Images;
    private final ArrayList<String> hairdressers;

    private final Context context;

    public DataAdapter(Context context, ArrayList<ArrayList<String>> images, ArrayList<String> hairdressers) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.Images = images;
        this.hairdressers = hairdressers;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loaded_hairdresser, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            //System.out.println(((ItemViewHolder) holder).imageView.toString());
            ((ItemViewHolder) holder).textView.setText(hairdressers.get(position));

            horizontalView(((ItemViewHolder) holder), position);

        } else {
            showLoadingView((LoadingViewHolder) holder, position);
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

    private void horizontalView(ItemViewHolder holder, int position) {
        HorizontalDataAdapter adapter = new HorizontalDataAdapter(Images.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageView hairdresserIcon;
        RecyclerView recyclerView;
        TextView textView;

        public ItemViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textLoaded);
            recyclerView = view.findViewById(R.id.horizontalRecycler);

            hairdresserIcon = view.findViewById(R.id.hairdresserIcon);
            hairdresserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent hairdresserProfile = new Intent(context, HairdresserActivity.class);
                    hairdresserProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(hairdresserProfile);
                }
            });
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


}


