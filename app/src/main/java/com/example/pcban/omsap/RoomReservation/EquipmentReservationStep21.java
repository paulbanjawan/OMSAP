package com.example.pcban.omsap.RoomReservation;

import android.app.AlertDialog;
import android.app.Fragment;
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

/**
 * Created by pcban on 29 May 2017.
 */

public class EquipmentReservationStep21 extends Fragment {
    List<Rooms> roomList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest;
    com.android.volley.RequestQueue requestQueue;
    CollapsingToolbarLayout toolbarLayout;
    View ChildView;
    int GetItemPosition;
    ArrayList<String> RoomInfo;
    SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.room_search_result, container, false);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        roomList = new ArrayList<>();
        RoomInfo = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvRoomSearchList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar4);
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
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    String room_id = RoomInfo.get(GetItemPosition);
                    editor.putString("room_id", room_id);
                    editor.apply();
                    String test = sharedPref.getString("room_id","");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Reservation of Room")
                            .setMessage("Do you want to reserve this room?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new ReserveRoom().execute();
                                }
                            }).show();
                    Toast.makeText(getActivity(), test, Toast.LENGTH_LONG).show();
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

        String eventDate = sharedPref.getString("eventDate", "");
        String endDate = sharedPref.getString("endDate", "");
        String StartTime = sharedPref.getString("StartTime", "");
        String EndTime = sharedPref.getString("EndTime", "");

        String ipAddress = getString(R.string.ipAddress);
        String HTTP_JSON_URL = "http://" + ipAddress + "/itsq/omsap_mobile/searchAvailableRooms.php?eventDate=" + eventDate + "&endDate=" + endDate + "&StartTime=" + StartTime + "&EndTime=" + EndTime + "";

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

                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            Rooms GetDataAdapter2 = new Rooms();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setRoomID(json.getString("room_id"));
                GetDataAdapter2.setRoomBldg(json.getString("room_building"));
                GetDataAdapter2.setRoomNum(json.getString("room_number"));

                RoomInfo.add(json.getString("room_id"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            roomList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new EquipmentCardView1(roomList, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);

    }

    class ReserveRoom extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;
        SharedPreferences sharedPref    =   getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String OrgID                    =   sharedPref.getString("org_id", "");
        String eventDate                =   sharedPref.getString("eventDate", "");
        String endDate                  =   sharedPref.getString("endDate", "");
        String StartTime                =   sharedPref.getString("StartTime", "");
        String EndTime                  =   sharedPref.getString("EndTime", "");
        String roomID                  =   sharedPref.getString("room_id", "");

        String ipAddress                =   getString(R.string.ipAddress);
        String EDIT_URL                 =   "http://"+ipAddress+"/ITSQ/omsap_mobile/editProfile.php";
        JSONParser jParser              =   new JSONParser();
        boolean connected               =   false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Reserving please wait...");
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
                nameValuePairs.add(new BasicNameValuePair("org_id", OrgID));
                nameValuePairs.add(new BasicNameValuePair("room_id", roomID));
                nameValuePairs.add(new BasicNameValuePair("eventDate", eventDate));
                nameValuePairs.add(new BasicNameValuePair("endDate", endDate));
                nameValuePairs.add(new BasicNameValuePair("StartTime", StartTime));
                nameValuePairs.add(new BasicNameValuePair("EndTime", EndTime));
                JSONObject json = jParser.postJSONFromUrl("http://"+ipAddress+"/ITSQ/omsap_mobile/reserveroom.php", nameValuePairs);
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
            final FragmentManager fm = getFragmentManager();

            pDialog.dismiss();
            if(connected)
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Reservation")
                        .setMessage("You have successfully reserved this room")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                JSON_DATA_WEB_CALL();
                                fm.beginTransaction().replace(R.id.main,new EquipmentReservationStep21()).commit();


                            }
                        }).show();

            }
            else
            {
                pDialog.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("You can't connect to the server. Please check your internet connection")
                        .setCancelable(false)
                        .setPositiveButton("Okay", null).show();
            }
        }
    }
}