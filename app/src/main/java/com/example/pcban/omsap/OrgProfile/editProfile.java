package com.example.pcban.omsap.OrgProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.pcban.omsap.ConnectionManager;
import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * Created by pcban on 19 May 2017.
 */

public class editProfile extends Activity{
    BootstrapEditText etOrgName, etOrgAbbrev, etOrgMission, etOrgVision;
    BootstrapButton btnEditProfile;
    SharedPreferences sharedPref;
    ImageView ivOrgname,ivOrgMission,ivOrgVision,ivEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        sharedPref      =   getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        etOrgName       =   (BootstrapEditText) findViewById(R.id.etOrgName);
        etOrgAbbrev     =   (BootstrapEditText) findViewById(R.id.etOrgAbbrev);
        etOrgMission    =   (BootstrapEditText) findViewById(R.id.etOrgMission);
        etOrgVision     =   (BootstrapEditText) findViewById(R.id.etOrgVision);

        btnEditProfile  =   (BootstrapButton) findViewById(R.id.btnEditProfile);

        ivOrgname       =   (ImageView) findViewById(R.id.ivOrgName);
        ivOrgMission    =   (ImageView) findViewById(R.id.ivOrgMission);
        ivOrgVision     =   (ImageView) findViewById(R.id.ivOrgVision);
        ivEdit          =   (ImageView) findViewById(R.id.ivEdit);

        ivOrgname.setImageResource(R.drawable.team);
        ivOrgMission.setImageResource(R.drawable.flag);
        ivOrgVision.setImageResource(R.drawable.glasses);
        ivEdit.setImageResource(R.drawable.edit);

        etOrgName.setText(sharedPref.getString("organization_name", ""));
        etOrgAbbrev.setText(sharedPref.getString("organization_abbreviation", ""));
        etOrgMission.setText(sharedPref.getString("org_mission", ""));
        etOrgVision.setText(sharedPref.getString("org_vision", ""));

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(etOrgName.getText().toString().length() == 0 && etOrgAbbrev.getText().toString().length() == 0 && etOrgMission.getText().toString().length() == 0 && etOrgVision.getText().toString().length() == 0)
                {
                    etOrgName.setError("Organization Name Required!");
                    etOrgAbbrev.setError("This field is Required!");
                    etOrgMission.setError("Organization Mission is Required!");
                    etOrgVision.setError("Organization Vision is Required");

                }
                else
                {
                    if(etOrgName.getText().toString().length() == 0)
                    {
                        etOrgName.setError("Organization Name Required!");
                    }
                    else if (etOrgAbbrev.getText().toString().length() == 0)
                    {
                        etOrgAbbrev.setError("This field is Required!");
                    }
                    else if (etOrgMission.getText().toString().length() == 0)
                    {
                        etOrgMission.setError("Organization Mission is Required!");
                    }
                    else if (etOrgVision.getText().toString().length() == 0)
                    {
                        etOrgVision.setError("Organization Vision is Required!");
                    }
                    else
                    {
                        new EditProfile().execute();
                    }
                }
            }
        });
    }




    class EditProfile extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;
        SharedPreferences sharedPref    =   getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String OrgID                    =   sharedPref.getString("org_id", "");
        String OrgName                  =   etOrgName.getText().toString();
        String OrgAbbrev                =   etOrgAbbrev.getText().toString();
        String OrgMission               =   etOrgMission.getText().toString();
        String OrgVision                =   etOrgVision.getText().toString();
        String ipAddress                =   getString(R.string.ipAddress);
        String EDIT_URL                 =   "http://"+ipAddress+"/ITSQ/omsap_mobile/editProfile.php";
        JSONParser jParser              =   new JSONParser();
        boolean connected               =   false;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(editProfile.this);
            pDialog.setMessage("Updating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            ConnectionManager cm = new ConnectionManager();
            if(cm.CheckUrlConnection(EDIT_URL)) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("OrgName", OrgName));
                nameValuePairs.add(new BasicNameValuePair("OrgAbbrev", OrgAbbrev));
                nameValuePairs.add(new BasicNameValuePair("OrgMission", OrgMission));
                nameValuePairs.add(new BasicNameValuePair("OrgVision", OrgVision));
                nameValuePairs.add(new BasicNameValuePair("OrgID", OrgID));
                JSONObject json = jParser.postJSONFromUrl("http://"+ipAddress+"/ITSQ/omsap_mobile/editProfile.php", nameValuePairs);
                connected = true;
                return json;
            }
            else
            {
                connected = false;
            }

            return null;
        }

        protected void onPostExecute(JSONObject json)
        {
            if(connected)
            {
                new AlertDialog.Builder(editProfile.this)
                        .setTitle("Edit Profile")
                        .setMessage("Your profile has been updated successfully")
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
                new AlertDialog.Builder(editProfile.this)
                        .setTitle("Warning")
                        .setMessage("You can't connect to the server. Please check your internet connection")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        }


    }
}





  /*         sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.putString("organization_name", OrgName);
            editor.putString("organization_abbreviation", OrgAbbrev);
            editor.putString("org_mission", OrgMission);
            editor.putString("org_vision", OrgVision);
            editor.commit();*/