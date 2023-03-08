package com.example.tindercut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.misc.AsyncTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final LayoutInflater inflater;
    private final ArrayList<String> Images;

    public DataAdapter(Context context, ArrayList<String> images) {
        this.inflater = LayoutInflater.from(context);
        Images = images;
    }


    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_scrolling, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        String url = Images.get(position);
        new DownloadImageTask(holder.imageView).execute(url);

    }

    @Override
    public int getItemViewType(int position) {
        return Images.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {

        return Images == null ? 0 : Images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;
        ViewHolder(View view){
            super(view);
            imageView = new ImageView(view.getContext());
        }
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}


