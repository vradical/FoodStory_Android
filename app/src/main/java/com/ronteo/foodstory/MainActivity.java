package com.ronteo.foodstory;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.ronteo.foodstory.fragment.GMapFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity{

    private AccessTokenTracker accessTokenTracker;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    protected Button mLogout;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    protected GoogleMap mGoogleMap;

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
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    /*
                    case R.id.tab_home:
                        Fragment mapFragment = new GMapFragment();
                        changeFragment(mapFragment, R.color.colorPrimary);
                        break;
                    */
                    case R.id.tab_discover:
                        Fragment mapFragment = new GMapFragment();
                        changeFragment(mapFragment, R.color.colorPrimary);
                        break;
                        /*
                    case R.id.tab_profile:
                        Fragment chatFragment = new ChatFragment();
                        changeFragment(chatFragment, R.color.colorPrimary);
                        break;
                        */
                }
            }
        });

        //Temp Placeholder for logout
        mLogout = findViewById(R.id.setting_logout);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Logout from FoodStory?")
                        .setTitle("Logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LoginManager.getInstance().logOut();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    //Change fragment
    public void changeFragment(Fragment changeFragment, int color){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, changeFragment);
        fragmentTransaction.commit();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, color));
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
