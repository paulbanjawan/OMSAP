package com.example.pcban.omsap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.pcban.omsap.InternetDetector.InternetDetector;
import com.example.pcban.omsap.OrgProfile.editProfile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    BootstrapButton btnLogin;
    EditText etUsername, etPassword;
    private ProgressDialog pDialog;
    InternetDetector cd;
    Boolean isInternetPresent = false;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        etUsername = (EditText) findViewById(R.id.etUname);
        etPassword = (EditText) findViewById(R.id.etPass);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onClick(View v)
    {
        // creating connection detector class instance
        cd = new InternetDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (isInternetPresent)
        {
            switch (v.getId())
            {
                case R.id.btnLogin:
                    if (etUsername.getText().toString().length() == 0 && etPassword.getText().toString().length() == 0)
                    {
                        etUsername.setError("Input Username!");
                        etPassword.setError("Input Password");
                    }
                    else
                    {
                        if (etUsername.getText().toString().length() == 0)
                        {
                            etUsername.setError("Input Username!");
                        }
                        else if (etPassword.getText().toString().length() == 0)
                        {
                            etPassword.setError("Input Password");
                        }
                        else
                        {
                            new AttemptLogin().execute();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        else
        {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Warning")
                    .setMessage("You can't connect to the server. Please check your internet connection")
                    .setCancelable(false)
                    .setPositiveButton("Okay",null).show();
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        boolean failure = false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
                String ipAddress = getString(R.string.ipAddress);
             String LOGIN_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/login.php";

            // TODO Auto-generated method stub
            // here Check for success tag
            int success;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                SharedPreferences sharedPref = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = sharedPref.edit();

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                //getting DATA USER FROM DATABASE
                String org_id                       =   json.getString("org_id").toString();
                String org_username                 =   json.getString("org_username").toString();
                String organization_name            =   json.getString("organization_name");
                String organization_abbreviation    =   json.getString("organization_abbreviation");
                String org_mission                  =   json.getString("org_mission");
                String org_vision                   =   json.getString("org_vision");
                String org_password                 =   json.getString("org_password");

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());

                    Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                    //PUTTING THE DATA TO THE SESSION
                    editor.putString("org_id"                       ,   org_id);
                    editor.putString("org_username"                 ,   org_username);
                    editor.putString("organization_name"            ,   organization_name);
                    editor.putString("organization_abbreviation"    ,   organization_abbreviation);
                    editor.putString("org_mission"                  ,   org_mission);
                    editor.putString("org_vision"                   ,   org_vision);
                    editor.putString("org_password"                 ,   org_password);
                    editor.putBoolean("IsLoggedIn"                  ,   true);
                    editor.apply();

                    finish();
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Incorrect Details")
                            .setMessage("Incorrect Username/Password")
                            .setCancelable(false)
                            .setPositiveButton("Okay",null)

                            .show();
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * **/
        protected void onPostExecute(String message)
        {
            pDialog.dismiss();
            if (message != null){
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
            else
            {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Warning")
                        .setMessage("Wrong Username or Password")
                        .setCancelable(false)
                        .setPositiveButton("Okay",null ).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
