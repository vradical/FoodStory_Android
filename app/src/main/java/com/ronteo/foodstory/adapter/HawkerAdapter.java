package com.ronteo.foodstory.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ronteo.foodstory.HawkerActivity;
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
        ImageView hawkerStore;
        ImageView hawkerDelivery;
        LinearLayout hawkerWrapper;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.hawkerNameView = itemView.findViewById(R.id.hawker_card_name);
            this.hawkerDescView = itemView.findViewById(R.id.hawker_card_desc);
            this.hawkerImageView = itemView.findViewById(R.id.hawker_card_cover);
            this.hawkerWrapper = itemView.findViewById(R.id.hawker_card_wrapper);
            this.hawkerStore = itemView.findViewById(R.id.hawker_card_isstore);
            this.hawkerDelivery = itemView.findViewById(R.id.hawker_card_isdelivery);
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

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.hawkerNameView;
        TextView textViewVersion = holder.hawkerDescView;
        ImageView imageView = holder.hawkerImageView;
        ImageView storeView = holder.hawkerStore;
        ImageView deliveryView = holder.hawkerDelivery;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getDescription());
        new DownloadImage(imageView).execute(dataSet.get(listPosition).getCoverPhoto());

        if(dataSet.get(listPosition).isDelivery() && dataSet.get(listPosition).isStore()){
            storeView.setVisibility(View.VISIBLE);
            deliveryView.setVisibility(View.VISIBLE);
        }else if (dataSet.get(listPosition).isStore()){
            storeView.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)storeView.getLayoutParams();
            params.setMargins(10, 10, 10, 10);
            storeView.setLayoutParams(params);
        }else if (dataSet.get(listPosition).isDelivery()){
            deliveryView.setVisibility(View.VISIBLE);
        }

        holder.hawkerWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), HawkerActivity.class);
                i.putExtra("hawkerID", String.valueOf(dataSet.get(holder.getLayoutPosition()).getId()));
                view.getContext().startActivity(i);
            }
        });
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
