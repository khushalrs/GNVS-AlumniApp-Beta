package com.example.mymessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ImageViewHolder> {
    private ArrayList<String> imageList;
    public EventAdapter(ArrayList<String> imgList){
        imageList = imgList;
    }
    @NonNull
    @Override
    public EventAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ImageViewHolder holder, int position) {
        final String image = imageList.get(position);
        holder.mSetImage(image);
    }

    @Override
    public int getItemCount() {
        return imageList == null? 0: imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        public ImageViewHolder(View itenView){
            super(itenView);
            mImage = itemView.findViewById(R.id.recyclerImage);
        }
        public void mSetImage(String url){
            Picasso.get().load(url).fit().centerCrop().into(mImage);
        }
    }
}
