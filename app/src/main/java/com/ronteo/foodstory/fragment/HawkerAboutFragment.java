package com.ronteo.foodstory.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ronteo.foodstory.HawkerActivity;
import com.ronteo.foodstory.R;
import com.ronteo.foodstory.model.Hawker;

/**
 * A simple {@link Fragment} subclass.
 */
public class HawkerAboutFragment extends Fragment {


    public HawkerAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hawker_about, container, false);

        Hawker hawker = ((HawkerActivity)getActivity()).hawker;

        TextView hawkerAbout = view.findViewById(R.id.hawker_about_text);
        TextView hawkerStory = view.findViewById(R.id.hawker_story_text);

        String about = "Phone: " + hawker.getPhone() + " | Email: " + hawker.getEmail() + " | Address " + hawker.getWebsite();

        hawkerAbout.setText(about);
        hawkerStory.setText(hawker.getStory());

        return view;
    }

}
