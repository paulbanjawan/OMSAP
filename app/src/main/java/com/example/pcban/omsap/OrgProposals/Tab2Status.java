package com.example.pcban.omsap.OrgProposals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.BootstrapProgressBarGroup;
import com.example.pcban.omsap.OrgProfile.editProfile;
import com.example.pcban.omsap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcban on 13 May 2017.
 */

public class Tab2Status extends Fragment
{
    List<ProposalStatus> statusList;
    ArrayList<String> statusInfo;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest;
    com.android.volley.RequestQueue requestQueue ;
    View ChildView ;
    int GetItemPosition ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab2status, container, false);
        statusList                  =   new ArrayList<>();
        statusInfo                  =   new ArrayList<>();
        recyclerView                =   (RecyclerView) rootView.findViewById(R.id.rvStatusList);
        progressBar                 =   (ProgressBar) rootView.findViewById(R.id.progressBar3);
        recyclerViewlayoutManager   =   new LinearLayoutManager(getActivity());

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
                    String prop_id = statusInfo.get(GetItemPosition);


                    if(prop_id.equals("1"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Status Details")
                                .setMessage("This proposal is currently on review")
                                .setCancelable(false)
                                .setPositiveButton("Okay",null)
                                .show();
                    }
                    if(prop_id.equals("2"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Status Details")
                                .setMessage("Go to the comment tab to see the comments.")
                                .setCancelable(false)
                                .setPositiveButton("Okay",null)
                                .show();
                    }
                    if(prop_id.equals("3"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Status Details")
                                .setMessage("This proposal has been approved.")
                                .setCancelable(false)
                                .setPositiveButton("Okay",null)
                                .show();
                    }
                    if(prop_id.equals("0"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Status Details")
                                .setMessage("This proposal has not been reviewed.")
                                .setCancelable(false)
                                .setPositiveButton("Okay",null)
                                .show();
                    }

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
        String prop_id          =   getActivity().getIntent().getStringExtra("proposal_id");
        String ipAddress        =   getString(R.string.ipAddress);
        String HTTP_JSON_URL    =   "http://"+ipAddress+"/ITSQ/omsap_mobile/getProposalStatus.php?prop_id="+prop_id;

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
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            ProposalStatus GetDataAdapter2 = new ProposalStatus();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                /*GetDataAdapter2.setSCC_Status(json.getString("scc_status"));
                GetDataAdapter2.setACCOUNTING_Status(json.getString("accounting_status"));
                GetDataAdapter2.setEDO_Status(json.getString("edo_status"));
                GetDataAdapter2.setSADU_Status(json.getString("sadu_status"));
                GetDataAdapter2.setSDAS_Status(json.getString("sdas_status"));*/
                GetDataAdapter2.setEntity(json.getString("entity"));
                GetDataAdapter2.setStatus(json.getString("status"));
                statusInfo.add(json.getString("status"));


            } catch (JSONException e) {

                e.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
            statusList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new StatusCardView(statusList, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);

    }
}
