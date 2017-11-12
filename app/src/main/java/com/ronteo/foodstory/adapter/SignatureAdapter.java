package com.ronteo.foodstory.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ronteo.foodstory.HawkerActivity;
import com.ronteo.foodstory.R;
import com.ronteo.foodstory.model.Dish;
import com.ronteo.foodstory.util.DownloadImage;

import java.util.ArrayList;

/**
 * Created by ronteo on 12/11/17.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureAdapter.MyViewHolder> {

    private ArrayList<Dish> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dishNameView;
        TextView dishDescView;
        ImageView dishImageView;
        LinearLayout dishWrapper;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.dishNameView = itemView.findViewById(R.id.dish_card_name);
            this.dishDescView = itemView.findViewById(R.id.dish_card_desc);
            this.dishImageView = itemView.findViewById(R.id.dish_card_cover);
            this.dishWrapper = itemView.findViewById(R.id.dish_card_wrapper);
        }
    }

    public SignatureAdapter(ArrayList<Dish> data) {
        this.dataSet = data;
    }

    @Override
    public SignatureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.signature_card_layout, parent, false);

        SignatureAdapter.MyViewHolder myViewHolder = new SignatureAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SignatureAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.dishNameView;
        TextView textViewVersion = holder.dishDescView;
        ImageView imageView = holder.dishImageView;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getDescription());
        new DownloadImage(imageView).execute(dataSet.get(listPosition).getImage());
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
