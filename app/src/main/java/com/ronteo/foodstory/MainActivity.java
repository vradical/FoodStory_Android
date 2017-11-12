package com.ronteo.foodstory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.ronteo.foodstory.fragment.GMapFragment;
import com.ronteo.foodstory.fragment.HawkerFragment;
import com.ronteo.foodstory.fragment.ProfileFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity{

    public static SharedPreferences preferences;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize
        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        fragmentManager = getFragmentManager();

        //Check for login status
        isLoggedIn();

        //Setup Content Proper
        setContentView(R.layout.activity_main);

        //Navigation Bar
        BottomBar bottomBar = findViewById(R.id.bottom_Bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {

                    case R.id.tab_home:
                        Fragment hawkerFragment = new HawkerFragment();
                        changeFragment(hawkerFragment);
                        break;

                    case R.id.tab_discover:
                        Fragment mapFragment = new GMapFragment();
                        changeFragment(mapFragment);
                        break;

                    case R.id.tab_profile:
                        Fragment profileFragment = new ProfileFragment();
                        changeFragment(profileFragment);
                        break;
                }
            }
        });

    }

    //Change fragment
    public void changeFragment(Fragment changeFragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, changeFragment);
        fragmentTransaction.commit();
    }

    //Check for facebook login
    public void isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
