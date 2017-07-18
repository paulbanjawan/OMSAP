package com.example.pcban.omsap.OrgProfile;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcban on 30 Apr 2017.
 */

public class fragmentMyProfile extends Fragment implements View.OnClickListener
{
    TextView tvOrgName,tvOrgAbbrev,tvOrgMission,tvOrgVision,tvOrgNameBar;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONArray orgdetails = null;
    CollapsingToolbarLayout toolbarLayout;
    ImageView ivOrgname,ivOrgMission,ivOrgVision,ivEdit;

    private static final String TAG_PROPOSAL_DETAILS = "organizationDetails";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_profile_scrolling,container,false);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Typeface roboto_regular = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Roboto-Regular.ttf");
        toolbarLayout   =   (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout2);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            new getOrganizationDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else // Below Api Level 13
        {
            new getOrganizationDetails().execute();
        }

        //INITIALIZING TEXT VIEWS
        tvOrgName       =   (TextView) rootView.findViewById(R.id.tvOrgName);
        tvOrgAbbrev     =   (TextView) rootView.findViewById(R.id.tvOrgAbbrev);
        tvOrgMission    =   (TextView) rootView.findViewById(R.id.tvOrgMission);
        tvOrgVision     =   (TextView) rootView.findViewById(R.id.tvOrgVision);
        tvOrgNameBar    =   (TextView) rootView.findViewById(R.id.tvOrgNameBar);

        ivOrgname       =   (ImageView) rootView.findViewById(R.id.ivOrgName2);
        ivOrgMission    =   (ImageView) rootView.findViewById(R.id.ivOrgMission2);
        ivOrgVision     =   (ImageView) rootView.findViewById(R.id.ivOrgVision2);

        ivOrgname.setImageResource(R.drawable.team);
        ivOrgMission.setImageResource(R.drawable.flag);
        ivOrgVision.setImageResource(R.drawable.glasses);

        tvOrgName.setTypeface(roboto_regular);
        tvOrgAbbrev.setTypeface(roboto_regular);
        tvOrgMission.setTypeface(roboto_regular);
        tvOrgVision.setTypeface(roboto_regular);
        tvOrgNameBar.setTypeface(roboto_regular);



        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    public class Wrapper
    {
        public String organization_name;
        public String organization_abbreviation;
        public String org_mission;
        public String org_vision;
    }
    class getOrganizationDetails extends AsyncTask<String, String, Wrapper>
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String org_id = sharedPref.getString("org_id","");
        String ipAddress    = getString(R.string.ipAddress);
        String FETCHING_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/getOrgDetails.php";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Wrapper doInBackground(String... params)
        {
            Wrapper w = new Wrapper();
            try
            {
                List<NameValuePair> paramsFetch = new ArrayList<NameValuePair>();
                paramsFetch.add(new BasicNameValuePair("org_id", org_id));
                JSONObject jsons = jsonParser.makeHttpRequest(FETCHING_URL, "POST", paramsFetch);
                orgdetails = jsons.getJSONArray(TAG_PROPOSAL_DETAILS);

                Log.d("request!", "starting");

                for (int i = 0; i < orgdetails.length(); i++)
                {
                    JSONObject c = orgdetails.getJSONObject(i);
                    final String organization_name          =   c.getString("organization_name");
                    final String organization_abbreviation  =   c.getString("organization_abbreviation");
                    final String org_mission                =   c.getString("org_mission");
                    final String org_vision                 =   c.getString("org_vision");

                    // checking log for json response
                    Log.d("Login attempt", jsons.toString());

                    //TRANSFERRING DATA TO THE ONPOSTEXECUTE
                    w.organization_name         = organization_name;
                    w.organization_abbreviation = organization_abbreviation;
                    w.org_mission               = org_mission;
                    w.org_vision                = org_vision;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return w;
        }

        @Override
        protected void onPostExecute(Wrapper w)
        {
            tvOrgName.setText(w.organization_name);
            tvOrgAbbrev.setText(w.organization_abbreviation);
            tvOrgMission.setText(w.org_mission);
            tvOrgVision.setText(w.org_vision);
            tvOrgNameBar.setText(w.organization_abbreviation);
            toolbarLayout.setTitle(w.organization_name);

            pDialog.dismiss();
        }
    }
}
