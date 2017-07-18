package com.example.pcban.omsap.OrgProposals;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
 * Created by pcban on 30 Apr 2017.
 */

public class fragMyProposals extends Fragment
{
    List<orgproposals> orgproposalsList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest ;
    com.android.volley.RequestQueue requestQueue ;
    CollapsingToolbarLayout toolbarLayout;
    View ChildView ;
    int GetItemPosition ;
    ArrayList<String> OrgInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_scrolling,container,false);

        SharedPreferences sharedPref            =   getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String organization_name                =   sharedPref.getString("organization_name","");
        String organization_abbreviation        =   sharedPref.getString("organization_abbreviation","");
        CollapsingToolbarLayout toolbarLayout   =   (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        orgproposalsList                        =   new ArrayList<>();
        OrgInfo                                 =   new ArrayList<>();
        recyclerView                            =   (RecyclerView) rootView.findViewById(R.id.recyclerView1);
        progressBar                             =   (ProgressBar) rootView.findViewById(R.id.progressBar1);
        recyclerViewlayoutManager               =   new LinearLayoutManager(getActivity());

        toolbarLayout.setTitle("Activity Proposals("+organization_abbreviation+")");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        JSON_DATA_WEB_CALL();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()

        {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener()
            {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent)
                {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent)
            {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent))
                {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    String prop_id = OrgInfo.get(GetItemPosition);

                    //SENDING THE ORG ID TO ANOTHER FRAGMENT
                    Intent intent = new Intent(getActivity(), ProposalDetails.class);
                    intent.putExtra("proposal_id",prop_id);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return rootView;
    }
    public void JSON_DATA_WEB_CALL(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String org_id = sharedPref.getString("org_id","");
        String ipAddress    = getString(R.string.ipAddress);
        String HTTP_JSON_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/getproposals.php?org_id="+org_id;

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);

                        new AlertDialog.Builder(getActivity())
                                .setTitle("No Proposal")
                                .setMessage("This Organization doesnt have any Proposals")
                                .setCancelable(true)
                                .setNegativeButton("Okay", null)
                                .show();
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            orgproposals GetDataAdapter2 = new orgproposals();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setProposalTitle(json.getString("proposal_title"));
                GetDataAdapter2.setOrgDateSent(json.getString("date_sent"));
                GetDataAdapter2.setProposalID(json.getString("prop_id"));
                GetDataAdapter2.setOrgGeneralObjective(json.getString("general_objective"));

                OrgInfo.add(json.getString("prop_id"));


            } catch (JSONException e) {

                e.printStackTrace();
            }
            orgproposalsList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new RecyclerViewCardViewAdapter(orgproposalsList, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);

    }


}
