package com.ronteo.foodstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ronteo.foodstory.R;
import com.ronteo.foodstory.model.Hawker;
import com.ronteo.foodstory.util.DownloadImage;

import java.util.ArrayList;

/**
 * Created by ronteo on 12/11/17.
 */

public class HawkerAdapter extends RecyclerView.Adapter<HawkerAdapter.MyViewHolder> {

    private ArrayList<Hawker> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hawkerNameView;
        TextView hawkerDescView;
        ImageView hawkerImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.hawkerNameView = (TextView) itemView.findViewById(R.id.hawkerNameView);
            this.hawkerDescView = (TextView) itemView.findViewById(R.id.hawkerDescView);
            this.hawkerImageView = (ImageView) itemView.findViewById(R.id.hawkerImageView);
        }
    }

    public HawkerAdapter(ArrayList<Hawker> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hawker_card_layout, parent, false);

        //view.setOnClickListener(HawkerFragment.);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.hawkerNameView;
        TextView textViewVersion = holder.hawkerDescView;
        ImageView imageView = holder.hawkerImageView;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getDescription());
        new DownloadImage(imageView).execute(dataSet.get(listPosition).getCoverPhoto());
    }

    @Override
    public int getItemCount() {
        if(dataSet == null || dataSet.isEmpty()){
            return 0;
        }else{
            return dataSet.size();
        }
    }

}
