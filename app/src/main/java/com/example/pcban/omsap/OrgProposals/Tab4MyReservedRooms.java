package com.example.pcban.omsap.OrgProposals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.support.v4.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pcban.omsap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcban on 29 May 2017.
 */

public class Tab4MyReservedRooms extends android.support.v4.app.Fragment
{
    List<ReservedRooms> reservationList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest;
    com.android.volley.RequestQueue requestQueue;
    View ChildView;
    int GetItemPosition;
    ArrayList<String> reservationInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.org_room_reservations, container, false);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
/*
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Reserved Rooms");
*/
        reservationList = new ArrayList<>();
        reservationInfo = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvOrgReservedRoom);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar5);
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

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    String prop_id = reservationInfo.get(GetItemPosition);

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
        String HTTP_JSON_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/getProposalRoom.php?prop_id="+prop_id;

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
                            /*new AlertDialog.Builder(getActivity())
                                    .setTitle("No Reservation")
                                    .setMessage("This proposal doesnt have any reserved room")
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
                                .setMessage("This proposal doesnt have any reserved room")
                                .setCancelable(true)
                                .setNegativeButton("Okay", null)
                                .show();*/
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {
//        if(array.length()>0)
//        {
            for (int i = 0; i < array.length(); i++) {

                ReservedRooms GetDataAdapter2 = new ReservedRooms();

                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);

                    GetDataAdapter2.setRoomID(json.getString("room_id"));
//                GetDataAdapter2.setRoomBldg(json.getString("room_building"));
                    GetDataAdapter2.setRoomNum(json.getString("room_number"));
                    GetDataAdapter2.setRsvStatus(json.getString("reservation_status"));
                    GetDataAdapter2.setRsvID(json.getString("room_reserve_id"));
                    GetDataAdapter2.setRsvStartDate(json.getString("date_reserved"));
                    GetDataAdapter2.setRsvEndDate(json.getString("date_end"));
                    GetDataAdapter2.setRsvStartTime(json.getString("time_start"));
                    GetDataAdapter2.setRsvRsvEndTime(json.getString("time_end"));
                    GetDataAdapter2.setRsvStatusName(json.getString("reservation_name"));
                    reservationInfo.add(json.getString("room_reserve_id"));

                } catch (JSONException e) {

                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);


                }
                reservationList.add(GetDataAdapter2);

            }
            recyclerViewadapter = new MyReservedRoomsCardView(reservationList, getActivity());

            recyclerView.setAdapter(recyclerViewadapter);


        /*}else
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Reservation")
                    .setMessage("This proposal doesnt have any reserved room")
                    .setCancelable(true)
                    .setNegativeButton("Okay", null)
                    .show();
        }*/


    }
}
