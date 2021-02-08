package com.krys.exoplayer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.krys.exoplayer.R;
import com.krys.exoplayer.model.ModelVideo;
import com.krys.exoplayer.utils.CommonUtils;

import java.util.ArrayList;


public class AdapterVideoList extends RecyclerView.Adapter<AdapterVideoList.MyViewHolder> {

    private ArrayList<ModelVideo> videosList = new ArrayList<ModelVideo>();
    private final Context context;
    private EventListener eventListener;

    public AdapterVideoList(Context context){
        this.context = context;
    }

    public void addAll(ArrayList<ModelVideo> videosList){
        this.videosList = videosList;
        notifyDataSetChanged();
    }

    public interface EventListener {
        void onVideoClicked(int position);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ModelVideo getItemData(int position) {
        return videosList.get(position);
    }

    @NonNull
    @Override
    public AdapterVideoList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVideoList.MyViewHolder holder, int position) {
        final ModelVideo item = videosList.get(position);
        holder.tv_title.setText(item.getTitle());
        holder.tv_duration.setText(item.getDuration());

        CommonUtils.showImage(context, String.valueOf(item.getData()), holder.imgView_thumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventListener!=null){
                    eventListener.onVideoClicked(position);
                    Log.e("videoId", "onClick: "+item.getId() );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView_thumbnail;
        TextView tv_title, tv_duration;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            imgView_thumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
