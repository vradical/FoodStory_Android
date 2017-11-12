package com.ronteo.foodstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ronteo.foodstory.util.RestClient;

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
    private MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        mDialog = new MaterialDialog.Builder(this)
                .content(R.string.dialog_loading)
                .progress(true, 0)
                .cancelable(false)
                .build();

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
                                    mDialog.show();
                                    loginUser();
                                } catch (JSONException e) {
                                    displayDialog(R.string.dialog_error_facebook);
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

        RestClient.get("user/login", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /*
                try {

                    if (response.getBoolean("newUser")) {
                        //prompt for new user procedure
                    }

                } catch (JSONException e) {
                    displayDialog("Error retrieving information from server");
                    e.printStackTrace();
                }*/

                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e("error", t.toString());
                displayDialog(R.string.dialog_error_server);
                LoginManager.getInstance().logOut();
            }

        });
    }

    public void displayDialog(int message) {

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
