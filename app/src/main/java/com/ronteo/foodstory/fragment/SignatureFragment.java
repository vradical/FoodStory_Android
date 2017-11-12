package com.ronteo.foodstory.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ronteo.foodstory.HawkerActivity;
import com.ronteo.foodstory.R;
import com.ronteo.foodstory.adapter.SignatureAdapter;
import com.ronteo.foodstory.model.Dish;
import com.ronteo.foodstory.model.Hawker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<Dish> signatureList;

    public SignatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signature, container, false);

        Hawker hawker = ((HawkerActivity)getActivity()).hawker;

        recyclerView = (RecyclerView) view.findViewById(R.id.signature_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        for (Dish d : hawker.getDishList()) {
            if(d.isSignature()){
                signatureList.add(d);
            }
        }

        adapter = new SignatureAdapter(signatureList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
