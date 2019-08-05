package com.margo.mymovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.margo.mymovies.R;
import com.margo.mymovies.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailers;
    private OnTrailerClickListener onTrailerClickListener;

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false);

        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder viewHolder, int i) {
        Trailer trailer = trailers.get(i);
        viewHolder.textViewNameOfVideo.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    public interface OnTrailerClickListener {
        void onTrailerClick(String url);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewNameOfVideo;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameOfVideo = itemView.findViewById(R.id.textViewNameOfVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTrailerClickListener != null){
                        onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
