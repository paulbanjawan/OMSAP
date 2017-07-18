package com.example.pcban.omsap.sendingProposal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pcban.omsap.R;

/**
 * Created by pcban on 30 Apr 2017.
 */

public class Uploading_page extends Fragment implements View.OnClickListener
{
    Button btnSelectFile;
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.send_proposal);
//        btnSelectFile = (Button) findViewById(R.id.btnSelectFile);
//        btnSelectFile.setOnClickListener(this);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.send_proposal,container,false);
        btnSelectFile = (Button) rootView.findViewById(R.id.btnSelectFile);
        btnSelectFile.setOnClickListener(this);
        return rootView;

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSelectFile:
                Intent i = new Intent(getActivity(), FileChooser.class);
                i.putExtra("idfilechooser","1");
                startActivity(i);
                break;
        }
    }
}