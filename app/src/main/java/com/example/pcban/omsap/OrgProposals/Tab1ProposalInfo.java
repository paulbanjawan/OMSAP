package com.example.pcban.omsap.OrgProposals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcban on 13 May 2017.
 */

public class Tab1ProposalInfo extends Fragment
{

    TextView tvPropID,tvProposalFile,tvPropTitle,tvGenObjective,tvSpecObjective,tvBudget,tvDateSent;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONArray proposaldetails = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String TAG_PROPOSAL_DETAILS = "proposalDetails";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab1proposalinfo, container, false);
        tvDateSent      =   (TextView) rootView.findViewById(R.id.tvDateSent);
        tvPropTitle     =   (TextView) rootView.findViewById(R.id.tvTitleProposal);
        tvGenObjective  =   (TextView) rootView.findViewById(R.id.tvGenObjective);
        tvSpecObjective =   (TextView) rootView.findViewById(R.id.tvSpecObjective);
        tvBudget        =   (TextView) rootView.findViewById(R.id.tvProposedBudget);
        new getProposalInfo().execute();

        /*SharedPreferences sharedPref = getActivity().getSharedPreferences("ProposalInfo", Context.MODE_PRIVATE);
        String prop_id = sharedPref.getString("proposal_id","");
        tvPropTitle.setText(sharedPref.getString("proposal_id",""));
        tvGenObjective.setText(getActivity().getIntent().getStringExtra("proposal_id"));*/




        return rootView;
    }

    public class Wrapper
    {
        public String proposal_title;
        public String proposal_file;
        public String proposal_gen_objective;
        public String proposal_spec_objective;
        public String proposal_budget;
        public String proposal_date_sent;
        public String proposal_sender;
        public String proposal_date_approved;
        public String proposal_scc_status;
        public String proposal_sadu_status;
        public String proposal_accounting_status;
        public String proposal_edo_status;
    }
    class getProposalInfo extends AsyncTask<String, String, Wrapper>
    {

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
            SharedPreferences sharedPref = getActivity().getSharedPreferences("ProposalInfo", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sharedPref.edit();
//            String prop_id = sharedPref.getString("proposal_id","");

            String prop_id = getActivity().getIntent().getStringExtra("proposal_id");

            String ipAddress    = getString(R.string.ipAddress);
            String FETCHING_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/getProposalDetails.php";
            Wrapper w = new Wrapper();
            try
            {
                List<NameValuePair> paramsFetch = new ArrayList<NameValuePair>();
                paramsFetch.add(new BasicNameValuePair("proposal_id", prop_id));
                JSONObject jsons = jsonParser.makeHttpRequest(FETCHING_URL, "POST", paramsFetch);
                proposaldetails = jsons.getJSONArray(TAG_PROPOSAL_DETAILS);

                Log.d("request!", "starting");
                editor.clear();
                editor.apply();
                for (int i = 0; i < proposaldetails.length(); i++)
                {
                    JSONObject c = proposaldetails.getJSONObject(i);

                    final String proposal_id                    =   c.getString("prop_id");
                    final String proposal_file                  =   c.getString("proposal");
                    final String proposal_title                 =   c.getString("proposal_title");
                    final String proposal_gen_objective         =   c.getString("general_objective");
                    final String proposal_spec_objective        =   c.getString("specific_objective");
                    final String proposal_budget                =   c.getString("proposed_budget");
                    final String proposal_date_sent             =   c.getString("date_sent");
                    final String proposal_sender                =   c.getString("sent_by");
                    final String proposal_date_approved         =   c.getString("date_approved");
                    final String proposal_scc_status            =   c.getString("scc_approve");
                    final String proposal_sadu_status           =   c.getString("sadu_status");
                    final String proposal_accounting_status     =   c.getString("accounting_status");
                    final String proposal_edo_status            =   c.getString("edo_status");

                    w.proposal_title = proposal_title;
                    w.proposal_gen_objective = proposal_gen_objective;
                    w.proposal_spec_objective = proposal_spec_objective;
                    w.proposal_budget = proposal_budget;
                    w.proposal_date_sent = proposal_date_sent;

                    editor.putString("scc_status",proposal_title);
                    editor.apply();
                    editor.commit();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return w;
        }
        protected void onPostExecute(Wrapper w)
        {
            tvDateSent.setText(w.proposal_date_sent);
            tvPropTitle.setText(w.proposal_title);
            tvGenObjective.setText(w.proposal_gen_objective);
            tvSpecObjective.setText(w.proposal_spec_objective);
            tvBudget.setText(w.proposal_budget);
            pDialog.dismiss();

        }
    }


}
