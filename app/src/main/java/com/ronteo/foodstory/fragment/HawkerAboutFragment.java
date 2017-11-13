package com.ronteo.foodstory.fragment;


import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ronteo.foodstory.HawkerActivity;
import com.ronteo.foodstory.R;
import com.ronteo.foodstory.model.Hawker;

import java.util.List;

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


        String coordAddress = "null";

        /*

        //Problem with timeout

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        if(hawker.getAddLat() != 0 && hawker.getAddLong() != 0) {
            try {
                address = coder.getFromLocation(hawker.getAddLat(), hawker.getAddLong(), 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(0);
                coordAddress = location.getLocality()+ ", " + location.getPostalCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        String about = "Phone: " + hawker.getPhone() + " | Email: " + hawker.getEmail() + " | Address: " + coordAddress;

        hawkerAbout.setText(about);
        hawkerStory.setText(hawker.getStory());

        return view;
    }

}
