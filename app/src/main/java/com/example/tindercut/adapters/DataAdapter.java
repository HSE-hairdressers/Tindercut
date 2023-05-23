package com.example.tindercut.adapters;

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

import com.bumptech.glide.Glide;
import com.example.tindercut.HairdresserActivity;
import com.example.tindercut.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter for nested recyclerviews inside scrolling activivty
 * The higher level of nesting
 */
public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final LayoutInflater inflater;
    private final ArrayList<ArrayList<String>> Images;
    private final ArrayList<String> hairdressers;

    private final ArrayList<String> userId;

    private final Context context;
    private final HashMap<String, ArrayList<String>> extras;

    /**
     * Конструктор
     *
     * @param context      App context
     * @param images       ArrayList of ArrayLists with hairdressers uploaded pics
     * @param hairdressers ArrayList of hairdressers
     * @param userId
     * @param extras       Hashmap of profile icons
     */
    public DataAdapter(Context context, ArrayList<ArrayList<String>> images, ArrayList<String> hairdressers, ArrayList<String> userId, HashMap<String, ArrayList<String>> extras) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.Images = images;
        this.hairdressers = hairdressers;
        this.userId = userId;
        this.extras = extras;
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
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loaded_hairdresser, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
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
        if (holder instanceof ItemViewHolder) {

            String url = extras.get(hairdressers.get(position)).get(0);

            //System.out.println(((ItemViewHolder) holder).imageView.toString());
            ((ItemViewHolder) holder).textView.setText(hairdressers.get(position));
            Glide.with(((ItemViewHolder) holder).hairdresserIcon.getContext()).load(url).into(((ItemViewHolder) holder).hairdresserIcon);

            horizontalView(((ItemViewHolder) holder), position);

        } else {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }

    /**
     * Returns type of required position
     * @param position position to query
     * @return type of position
     */
    @Override
    public int getItemViewType(int position) {
        return Images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /**
     * Counts items
     * @return number of items
     */
    @Override
    public int getItemCount() {

        return hairdressers == null ? 0 : hairdressers.size();
    }

    private void horizontalView(ItemViewHolder holder, int position) {
        HorizontalDataAdapter adapter = new HorizontalDataAdapter(Images.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    /**
     * Empty func for actions with loading animation
     * @param viewHolder
     * @param position
     */
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    /**
     * Describes an item view and metadata about its place within the RecyclerView
     */
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView hairdresserIcon;
        RecyclerView recyclerView;
        TextView textView;

        /**
         * Implements listener for opening hairdresser profile
         * @param view required View
         */
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
                    hairdresserProfile.putExtra("id", userId.get(getAdapterPosition()));
                    hairdresserProfile.putExtra("name", hairdressers.get(getAdapterPosition()));
                    hairdresserProfile.putExtra("icon", extras.get(hairdressers.get(getAdapterPosition())).get(0));
                    context.startActivity(hairdresserProfile);
                }
            });
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

    /**
     * Implements nesting of recyclerviews
     */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontalRecycler);
        }
    }


}


