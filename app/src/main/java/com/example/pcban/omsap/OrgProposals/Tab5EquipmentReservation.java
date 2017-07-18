package com.example.pcban.omsap.OrgProposals;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pcban.omsap.ConnectionManager;
import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.support.v4.app.Fragment;

/**
 * Created by pcban on 11 Jun 2017.
 */

public class Tab5EquipmentReservation extends Fragment
{
    List<Equipments> equipmentList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest;
    com.android.volley.RequestQueue requestQueue;
    CollapsingToolbarLayout toolbarLayout;
    View ChildView;
    int GetItemPosition;
    ArrayList<String> EquipmentInfo;
    SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.equipment_reservations, container, false);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        equipmentList = new ArrayList<>();
        EquipmentInfo = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvEquipment);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar6);
        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);


        JSON_DATA_WEB_CALL();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()

        {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent))
                {



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

    public void JSON_DATA_WEB_CALL() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String prop_id = getActivity().getIntent().getStringExtra("proposal_id");

        String ipAddress = getString(R.string.ipAddress);
        String HTTP_JSON_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/propEquipments.php?prop_id="+prop_id;

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);
                        if(response.length()>0)
                        {
                            JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        }
                        else
                        {
                           /* new AlertDialog.Builder(getActivity())
                                    .setTitle("No Reservation")
                                    .setMessage("This proposal doesnt have any reserved equipments")
                                    .setCancelable(true)
                                    .setNegativeButton("Okay", null)
                                    .show();*/
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                         /*new AlertDialog.Builder(getActivity())
                            .setTitle("No Reservation")
                            .setMessage("This proposal doesnt have any reserved equipments")
                            .setCancelable(true)
                            .setNegativeButton("Okay", null)
                            .show();*/
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {
/*
        Toast.makeText(getActivity(),array.toString(),Toast.LENGTH_LONG).show();
*/
/*
        if(array.length()>0) {
*/
            for (int i = 0; i < array.length(); i++) {

                Equipments GetDataAdapter2 = new Equipments();

                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);

                    GetDataAdapter2.setEqID(json.getString("equipment_id"));
                    GetDataAdapter2.setEqName(json.getString("equipment_name"));
                    GetDataAdapter2.setEqQuantity(json.getString("equipment_quantity"));
                    GetDataAdapter2.setDate(json.getString("date_reserved"));
                    GetDataAdapter2.setStatus(json.getString("reservation_status"));

                    EquipmentInfo.add(json.getString("equipment_id"));

                } catch (JSONException e) {

                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                   /* new AlertDialog.Builder(getActivity())
                            .setTitle("No Reservation")
                            .setMessage("This proposal doesnt have any reserved equipments")
                            .setCancelable(true)
                            .setNegativeButton("Okay", null)
                            .show();*/
                }
                equipmentList.add(GetDataAdapter2);
            }

            recyclerViewadapter = new EquipmentCardView(equipmentList, getActivity());

            recyclerView.setAdapter(recyclerViewadapter);
       /* }
        else
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Reservation")
                    .setMessage("This proposal doesnt have any reserved equipments")
                    .setCancelable(true)
                    .setNegativeButton("Okay", null)
                    .show();
        }*/

    }


}
