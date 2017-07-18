package com.example.pcban.omsap.OrgProposals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
 * Created by pcban on 13 May 2017.
 */

public class Tab3Comments extends Fragment
{
    List<Comments> commentList;
    ArrayList<String> commentInfo;
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

        View rootView = inflater.inflate(R.layout.tab3comments, container, false);
        commentList                 =   new ArrayList<>();
        commentInfo                 =   new ArrayList<>();
        recyclerView                =   (RecyclerView) rootView.findViewById(R.id.rvCommentList);
        progressBar                 =   (ProgressBar) rootView.findViewById(R.id.progressBar2);
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

                   /* GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    String prop_id = commentInfo.get(GetItemPosition);*/

                    //SENDING THE ORG ID TO ANOTHER FRAGMENT
                /*    Intent intent = new Intent(getActivity(), ProposalDetails.class);
                    intent.putExtra("proposal_id",prop_id);
                    startActivity(intent);*/
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
//        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String prop_id          =   getActivity().getIntent().getStringExtra("proposal_id");
        String ipAddress        =   getString(R.string.ipAddress);
        String HTTP_JSON_URL    =   "http://"+ipAddress+"/ITSQ/omsap_mobile/getComments.php?prop_id="+prop_id;

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
                                    .setTitle("No Comments")
                                    .setMessage("This proposal doesnt have any comments")
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
                                .setTitle("No Comments")
                                .setMessage("This proposal doesnt have any comments")
                                .setCancelable(true)
                                .setNegativeButton("Okay", null)
                                .show();*/
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

/*
        if(array.length()>0) {
*/
            for (int i = 0; i < array.length(); i++) {

                Comments GetDataAdapter2 = new Comments();

                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);

                    GetDataAdapter2.setComment(json.getString("comment"));
                    GetDataAdapter2.setCommentAuthor(json.getString("office_abbreviation"));
                    GetDataAdapter2.setCommentDate(json.getString("date"));

                    //                commentInfo.add(json.getString("prop_id"));


                } catch (JSONException e) {

                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("No Comments")
                            .setMessage("This proposal doesnt have any comments")
                            .setCancelable(true)
                            .setNegativeButton("Okay", null)
                            .show();
                }
                commentList.add(GetDataAdapter2);
            }


            recyclerViewadapter = new CommentCardView(commentList, getActivity());

            recyclerView.setAdapter(recyclerViewadapter);
      /*  }
        else
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Comments")
                    .setMessage("This proposal doesnt have any comments")
                    .setCancelable(true)
                    .setNegativeButton("Okay", null)
                    .show();
        }*/

    }
}
