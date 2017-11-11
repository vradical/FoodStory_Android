package com.ronteo.foodstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        preferences = MainActivity.preferences;
        editor = preferences.edit();

        //INITIALIZE FACEBOOK CALLBACK
        callbackManager = CallbackManager.Factory.create();

        //CREATE FACEBOOK LOGIN BUTTON
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    System.out.println(object);
                                    editor.putString("user_fbsession", object.getString("id"));
                                    editor.putString("user_email", object.getString("email"));
                                    editor.putString("user_name", object.getString("name"));
                                    editor.apply();
                                    editor.commit();
                                    loginUser();
                                } catch (JSONException e) {
                                    displayDialog("Failed to connect to Facebook");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //PREPARE QUERY TO LOGIN USER
    public void loginUser() {

        String fbId = preferences.getString("user_fbsession", "");
        String name = preferences.getString("user_name", "");
        String email = preferences.getString("user_email", "");

        RequestParams params = new RequestParams();

        params.put("name", name);
        params.put("fbLogin", fbId);
        params.put("email", email);

        invokeWS(params);

    }

    //SEND QUERY TO ATHENA WEB SERVICE
    public void invokeWS(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.WEB_SERVICE+"user/login", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    if (response.getBoolean("newUser")) {
                        //prompt for new user procedure
                    }

                } catch (JSONException e) {
                    displayDialog("Error retrieving information from server");
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
            }

            @Override
            public void onFinish() {
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                LoginManager.getInstance().logOut();
            }

        });
    }

    public void displayDialog(String message) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.this);
        (builder).setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


}
