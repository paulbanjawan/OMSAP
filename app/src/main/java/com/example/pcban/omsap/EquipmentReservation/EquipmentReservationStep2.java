package com.example.pcban.omsap.EquipmentReservation;

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
import com.example.pcban.omsap.RoomReservation.EquipmentReservationStep21;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by pcban on 11 Jun 2017.
 */

public class EquipmentReservationStep2 extends Fragment
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
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    final String equipment_id = EquipmentInfo.get(GetItemPosition);
                    final String[] split = equipment_id.split(",");

                    editor.putString("equipment_id", split[0]);
                    editor.apply();

                    Toast.makeText(getActivity(),equipment_id,
                            Toast.LENGTH_SHORT).show();



                    final EditText taskEditText = new EditText(getActivity());
                    if(split[1].equals("0"))
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Out of Stock")
                                .setMessage("This equipment is unavailable")
                                .setCancelable(true)
                                .setNegativeButton("Okay", null)
                                .show();
                    }
                    else
                    {
                          new AlertDialog.Builder(getActivity())
                            .setTitle("Reservation of Equipment")
                            .setMessage("Please enter the quantity")
                            .setCancelable(true)
                            .setView(taskEditText)

                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                    SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_MULTI_PROCESS);
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    String task = String.valueOf(taskEditText.getText());
                                    Integer quantity = Integer.valueOf(task);
                                    if(taskEditText.getText().toString().length() == 0)
                                    {
                                        Toast.makeText(getActivity(),"Input Quantity",
                                                Toast.LENGTH_SHORT).show();
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Invalid Input")
                                                .setMessage("Input Quantity (Maximum : 3) ")
                                                .setCancelable(true)
                                                .setNegativeButton("Okay", null)
                                                .show();


                                    }
                                    else if(quantity>=Integer.valueOf(split[2]))
                                    {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Invalid Input")
                                                .setMessage("not enough available stocks")
                                                .setCancelable(true)
                                                .setNegativeButton("Okay", null)
                                                .show();
                                    }
                                    else if(quantity>4)
                                    {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Invalid Input")
                                                .setMessage("Maximum Quantity is 3")
                                                .setCancelable(true)
                                                .setNegativeButton("Okay", null)
                                                .show();
                                    }
                                    else
                                    {
                                        editor.putString("quantity",task);
                                        editor.apply();
                                        new EquipmentReservationStep2.ReserveEquipment().execute();
                                    }


                                }
                            }).show();
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

    public void JSON_DATA_WEB_CALL() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String eventDate = sharedPref.getString("eventDate", "");
        String prop = sharedPref.getString("prop_id", "");
        String []split=prop.split(Pattern.quote("."));
        String ipAddress = getString(R.string.ipAddress);
        String HTTP_JSON_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/searchAvailableEquipments.php?startdate="+eventDate+"&prop="+split[0];

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

            Equipments GetDataAdapter2 = new Equipments();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setEqID(json.getString("equipment_id"));
                GetDataAdapter2.setEqName(json.getString("equipment_name"));
                GetDataAdapter2.setEqQuantity(json.getString("quantity"));
                GetDataAdapter2.setEqAvailStock(json.getString("available_stocks"));
                GetDataAdapter2.setMine(json.getString("mine"));

                EquipmentInfo.add(json.getString("equipment_id")+","+json.getString("available_stocks")+","+json.getString("quantity"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            equipmentList.add(GetDataAdapter2);
        }

        recyclerViewadapter = new EquipmentCardView(equipmentList, getActivity());

        recyclerView.setAdapter(recyclerViewadapter);

    }

    class ReserveEquipment extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;
        SharedPreferences sharedPref    =   getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String OrgID                    =   sharedPref.getString("org_id", "");
        String eventDate                =   sharedPref.getString("eventDate", "");
        String endDate                  =   sharedPref.getString("endDate", "");
        String StartTime                =   sharedPref.getString("StartTime", "");
        String EndTime                  =   sharedPref.getString("EndTime", "");
        String prop_id                   =   sharedPref.getString("prop_id", "");
        String EquipmentID              =   sharedPref.getString("equipment_id", "");
        String quantity                 =   sharedPref.getString("quantity", "");


        String ipAddress                =   getString(R.string.ipAddress);
        String EDIT_URL                 =   "http://"+ipAddress+"/ITSQ/omsap_mobile/reserveequipment.php";
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
                nameValuePairs.add(new BasicNameValuePair("prop_id", prop_id));
                nameValuePairs.add(new BasicNameValuePair("eventDate", eventDate));
                nameValuePairs.add(new BasicNameValuePair("endDate", endDate));
                nameValuePairs.add(new BasicNameValuePair("StartTime", StartTime));
                nameValuePairs.add(new BasicNameValuePair("EndTime", EndTime));
                nameValuePairs.add(new BasicNameValuePair("equipment_id", EquipmentID));
                nameValuePairs.add(new BasicNameValuePair("quantity", quantity));

                JSONObject json = jParser.postJSONFromUrl("http://"+ipAddress+"/ITSQ/omsap_mobile/reserveequipment.php", nameValuePairs);
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
                        .setMessage("You have successfully reserved this equipment")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                JSON_DATA_WEB_CALL();
                                fm.beginTransaction().replace(R.id.main,new EquipmentReservationStep2()).commit();


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
