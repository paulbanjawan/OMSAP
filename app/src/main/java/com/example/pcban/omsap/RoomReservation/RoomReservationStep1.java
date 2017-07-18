package com.example.pcban.omsap.RoomReservation;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.pcban.omsap.JSONParser;
import com.example.pcban.omsap.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by pcban on 28 May 2017.
 */

public class RoomReservationStep1 extends Fragment
{
    EditText etSelectDate,etEndDate,etTimeStart,etTimeEnd;
    Button btnSearch;
    Spinner spActivityProposals;
    ArrayAdapter adapter;
    ProgressDialog pDialog;
    JSONArray proposaldetails = null;
    JSONParser jsonParser = new JSONParser();
    final Calendar myCalendar = Calendar.getInstance();
    ArrayList<String> listItems = new ArrayList<>();
    private static final String TAG_PROPOSAL_DETAILS = "scheduleDetails";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView   =   inflater.inflate(R.layout.reservation_room_step1,container,false);
        etSelectDate    =   (EditText) rootView.findViewById(R.id.etSelectDate);
        etEndDate       =   (EditText) rootView.findViewById(R.id.etEndDate);
        etTimeStart     =   (EditText) rootView.findViewById(R.id.etTimeStart);
        etTimeEnd       =   (EditText) rootView.findViewById(R.id.etTimeEnd);
        btnSearch       =   (Button) rootView.findViewById(R.id.btnSearch);

        spActivityProposals = (Spinner) rootView.findViewById(R.id.spProposals);
        adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listItems);
        spActivityProposals.setAdapter(adapter);

        spActivityProposals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                new getProposalInfo().execute();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.main,new EquipmentReservationStep21()).commit();
            }
        });

        etSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etTimeStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etTimeStart.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time Start");
                mTimePicker.show();

            }
        });

        etTimeEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etTimeEnd.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time Start");
                mTimePicker.show();

            }
        });

        return rootView;
    }

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }

    };
    private void updateLabel()
    {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etSelectDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel2()
    {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEndDate.setText(sdf.format(myCalendar.getTime()));
    }
    public void onStart(){
        super.onStart();
        getApprovedProposals bt=new getApprovedProposals();
        bt.execute();
    }


    class getApprovedProposals extends AsyncTask<Void,Void,Void>
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String org_id = sharedPref.getString("org_id","");

        String ipAddress    =   getString(R.string.ipAddress);
        String URL          =   "http://"+ipAddress+"/ITSQ/omsap_mobile/getApprovedProposals.php?org_id="+org_id;
        ArrayList<String> list;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... params) {
/*
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("org_id", org_id));
*/
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                HttpResponse response = httpclient.execute(httppost);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
                //  result=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("prop_id")+"."+jsonObject.getString("proposal_title"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    /*class searchAvailableRooms extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;
        String ipAddress    =   getString(R.string.ipAddress);
        String SEARCH_URL  =  "http://"+ipAddress+"/ITSQ/omsap_mobile/searchAvailableRooms.php";
        JSONParser jParser  =   new JSONParser();
        boolean connected   =   false;
        String eventDate    =   etSelectDate.getText().toString();
        String endDate      =   etEndDate.getText().toString();
        String StartTime    =   etTimeStart.getText().toString();
        String EndTime      =   etTimeEnd.getText().toString();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Searching...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... params)
        {

            ConnectionManager cm = new ConnectionManager();
            if(cm.CheckUrlConnection(SEARCH_URL))
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("eventDate", eventDate));
                nameValuePairs.add(new BasicNameValuePair("endDate", endDate));
                nameValuePairs.add(new BasicNameValuePair("StartTime", StartTime));
                nameValuePairs.add(new BasicNameValuePair("EndTime", EndTime));
                JSONObject json = jParser.postJSONFromUrl(SEARCH_URL, nameValuePairs);
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
                new AlertDialog.Builder(getActivity())
                        .setTitle("Edit Profile")
                        .setMessage("Your profile has been updated successfully")
                        .setCancelable(false)
                        .setPositiveButton("Okay", null).show();
            }
            else
            {

            }
        }
    }*/


    public class Wrapper
    {
        public String proposal_start_date;
        public String proposal_end_date;
        public String proposal_start_time;
        public String proposal_end_time;
    }
    class getProposalInfo extends AsyncTask<String, String, RoomReservationStep1.Wrapper>
    {
        String prop = spActivityProposals.getSelectedItem().toString();

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
        protected RoomReservationStep1.Wrapper doInBackground(String... params)
        {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sharedPref.edit();


            String ipAddress    = getString(R.string.ipAddress);
            String FETCHING_URL = "http://"+ipAddress+"/ITSQ/omsap_mobile/getProposalSchedule.php";
            RoomReservationStep1.Wrapper w = new RoomReservationStep1.Wrapper();
            try
            {
                List<NameValuePair> paramsFetch = new ArrayList<NameValuePair>();
                paramsFetch.add(new BasicNameValuePair("prop", prop));
                JSONObject jsons = jsonParser.makeHttpRequest(FETCHING_URL, "POST", paramsFetch);
                proposaldetails = jsons.getJSONArray(TAG_PROPOSAL_DETAILS);

                Log.d("request!", "starting");
                for (int i = 0; i < proposaldetails.length(); i++)
                {
                    JSONObject c = proposaldetails.getJSONObject(i);

                    final String proposal_start_date    =   c.getString("proposal_start_date");
                    final String proposal_end_date      =   c.getString("proposal_end_date");
                    final String proposal_start_time    =   c.getString("proposal_start_time");
                    final String proposal_end_time      =   c.getString("proposal_end_time");

                    editor.putString("eventDate", proposal_start_date);
                    editor.putString("endDate", proposal_end_date);
                    editor.putString("StartTime", proposal_start_time);
                    editor.putString("EndTime", proposal_end_time);
                    editor.apply();

                    w.proposal_start_date = proposal_start_date;
                    w.proposal_end_date = proposal_end_date;
                    w.proposal_start_time = proposal_start_time;
                    w.proposal_end_time = proposal_end_time;
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return w;
        }
        protected void onPostExecute(RoomReservationStep1.Wrapper w)
        {
            etSelectDate.setText(w.proposal_start_date);
            etEndDate.setText(w.proposal_end_date);
            etTimeStart.setText(w.proposal_start_time);
            etTimeEnd.setText(w.proposal_end_time);
            pDialog.dismiss();
        }
    }


}
