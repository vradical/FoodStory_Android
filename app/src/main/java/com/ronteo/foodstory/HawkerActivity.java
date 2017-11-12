package com.ronteo.foodstory;

import android.app.Fragment;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ronteo.foodstory.adapter.HawkerAdapter;
import com.ronteo.foodstory.fragment.GMapFragment;
import com.ronteo.foodstory.fragment.HawkerFragment;
import com.ronteo.foodstory.fragment.ProfileFragment;
import com.ronteo.foodstory.model.Hawker;
import com.ronteo.foodstory.util.DownloadImage;
import com.ronteo.foodstory.util.RestClient;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HawkerActivity extends AppCompatActivity {

    private String hawkerID;
    private MaterialDialog mDialog;
    private Hawker hawker;

    private TextView hawkerProfileName;
    private ImageView hawkerCoverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        hawkerID = b.getString("hawkerID");

        mDialog = new MaterialDialog.Builder(this)
                .content(R.string.dialog_loading)
                .progress(true, 0)
                .cancelable(false)
                .build();

        mDialog.show();

        invokeWS();


    }

    public void invokeWS() {

        RestClient.get("hawker/get/"+ hawkerID, new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ObjectMapper mapper = new ObjectMapper();
                try {
                    hawker = mapper.readValue(responseBody, Hawker.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("Testing", hawker.toString());

                setContentView(R.layout.activity_hawker);

                hawkerProfileName = findViewById(R.id.hawker_profile_name);
                hawkerCoverImage = findViewById(R.id.hawker_cover_image);

                new DownloadImage(hawkerCoverImage).execute(hawker.getCoverPhoto());
                hawkerProfileName.setText(hawker.getName());

                //Navigation Bar
                BottomBar bottomBar = findViewById(R.id.hawker_mid_bar);
                bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelected(@IdRes int tabId) {
                        switch (tabId) {

                            case R.id.hawker_about:
                                break;

                            case R.id.hawker_signature:
                                break;

                            case R.id.hawker_dishes:
                                break;
                        }
                    }
                });

                mDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialog.dismiss();
                finish();

                Log.e("Testing", error.toString());
            }

        });
    }
}
