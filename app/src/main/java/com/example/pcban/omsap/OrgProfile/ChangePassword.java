package com.example.pcban.omsap.OrgProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pcban.omsap.ConnectionManager;
import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pcban on 24 May 2017.
 */

public class ChangePassword extends Activity implements View.OnClickListener{
    EditText currPass,newPass,conPass;
    ProgressDialog pDialog;
    Button btnChangePass;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        currPass        = (EditText) findViewById(R.id.etCurrPass);
        newPass         = (EditText) findViewById(R.id.etNewPass);
        conPass         = (EditText) findViewById(R.id.etConPass);
        btnChangePass   = (Button) findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String current_password = sharedPref.getString("org_password","").toString();
        String currentEnteredPass = currPass.getText().toString();
        switch (v.getId())
        {
            case R.id.btnChangePass:

                if(currPass.getText().toString().length()==0)
                {
                    currPass.setError("Insert Old password");
                }
                else if(newPass.getText().toString().length()==0)
                {
                    newPass.setError("Insert password");
                }
                else if(conPass.getText().toString().length()==0)
                {
                    conPass.setError("Insert password");
                }
                else if(!currentEnteredPass.equals(current_password))
                {
                    currPass.setError("Invalid old password");
                }
                else if(!conPass.getText().toString().equals(newPass.getText().toString()))
                {
                    conPass.setError("Password Does not match");
                    newPass.setError("Password Does not match");
                }
                else
                {
                    new ChangePass().execute();
                }
                break;
        }
    }


    class ChangePass extends AsyncTask<String,String,JSONObject> {
        String currentPassword = currPass.getText().toString();
        String newPassword = newPass.getText().toString();
        String ipAddress  =   getString(R.string.ipAddress);
        boolean connected               =   false;
        JSONParser jParser              =   new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setMessage("Change Password...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            String OrgID = sharedPref.getString("org_id","").toString();
            jsonParser = new JSONParser();
            Log.d("currentPassword", currentPassword);
            Log.d("newPassword", newPassword);
            ConnectionManager cm = new ConnectionManager();
            if(cm.CheckUrlConnection("http://"+ipAddress+"/ITSQ/omsap_mobile/changePassword.php")) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("OrgID", OrgID));
            nameValuePairs.add(new BasicNameValuePair("currentPassword", currentPassword));
            nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));


                JSONObject json = jParser.postJSONFromUrl("http://"+ipAddress+"/ITSQ/omsap_mobile/changePassword.php", nameValuePairs);
                connected = true;

                return json;
            }
            else
            {
                connected = false;
            }

            return null;
        }
     /*   protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            new AlertDialog.Builder(ChangePassword.this)
                    .setTitle("Change password")
                    .setMessage("Your password has been changed successfully")
                    .setNegativeButton("Okay", null)
                    .create().show();
        }*/

        protected void onPostExecute(JSONObject json)
        {
            if(connected)
            {
                pDialog.dismiss();
                new AlertDialog.Builder(ChangePassword.this)
                        .setTitle("Change password")
                        .setMessage("Your password has been changed successfully")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
            else
            {
                pDialog.dismiss();
                Toast.makeText(ChangePassword.this, "You can't connect to the server. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



