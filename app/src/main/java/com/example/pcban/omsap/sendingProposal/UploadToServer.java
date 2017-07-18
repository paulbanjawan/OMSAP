package com.example.pcban.omsap.sendingProposal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.pcban.omsap.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by pcban on 30 Apr 2017.
 */

public class UploadToServer extends Activity
{
    TextView messageText;
    BootstrapEditText etProposalTitle,etGenObjectives,etSpecObjectives,etBudget;
    EditText etStartDate,etEndDate,etStartTime,etTimeEnd;
    Button uploadButton;
    final Calendar myCalendar = Calendar.getInstance();

    int serverResponseCode  =   0;
    ProgressDialog dialog   =   null;
    String upLoadServerUri  =   null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_proposal_layout);

        SharedPreferences sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String path = sharedPref.getString("pathfile", "").toString();
        String name = sharedPref.getString("namefile", "").toString();

        final String uploadFilePath = path;
        final String uploadFileName = name;

        uploadButton        =   (Button)findViewById(R.id.uploadButton);
        messageText         =   (TextView)findViewById(R.id.messageText);
        etProposalTitle     =   (BootstrapEditText) findViewById(R.id.etTitleProposal);
        etGenObjectives     =   (BootstrapEditText) findViewById(R.id.etGenObjectives);
        etSpecObjectives    =   (BootstrapEditText) findViewById(R.id.etSpecObjectives);
        etBudget            =   (BootstrapEditText) findViewById(R.id.etBudget);
        etStartDate         =   (EditText) findViewById(R.id.etDateStart);
        etEndDate           =   (EditText) findViewById(R.id.etDateEnd);
        etStartTime         =   (EditText) findViewById(R.id.etTimeStart);
        etTimeEnd           =   (EditText) findViewById(R.id.etTimeEnd);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UploadToServer.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UploadToServer.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UploadToServer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etStartTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

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
                mTimePicker = new TimePickerDialog(UploadToServer.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etTimeEnd.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time End");
                mTimePicker.show();

            }
        });


        messageText.setText("Uploading file: "+uploadFileName+"'");
        /************* Php script path ****************/

        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(etProposalTitle.getText().toString().length() == 0 && etSpecObjectives.getText().toString().length() == 0 && etGenObjectives.getText().toString().length() == 0 && etBudget.getText().toString().length() == 0)
                {
                    etProposalTitle.setError("Proposal Title Required!");
                    etGenObjectives.setError("General Objectives Required!");
                    etSpecObjectives.setError("Specific Objectives Required!");
                    etBudget.setError("Proposed Budget Required");

                }
                else
                {
                    if(etProposalTitle.getText().toString().length() == 0)
                    {
                        etProposalTitle.setError("Proposal Title Required!");
                    }
                    else if (etSpecObjectives.getText().toString().length() == 0)
                    {
                        etSpecObjectives.setError("Specific Objectives Required!");
                    }
                    else if (etGenObjectives.getText().toString().length() == 0)
                    {
                        etGenObjectives.setError("General Objectives Required!");
                    }
                    else if (etBudget.getText().toString().length() == 0)
                    {
                        etBudget.setError("Proposed Budget Required");
                    }
                    else
                    {
                        dialog = ProgressDialog.show(UploadToServer.this, "", "Uploading file...", true);
                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        messageText.setText("uploading started.....");
                                    }
                                });
                                uploadFile(uploadFilePath);
                            }

                        }).start();

                        /*SharedPreferences sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

                        String upLoadServerUri = sharedPref.getString("upLoadServerUri", "").toString();
                        String org_id                   =   sharedPref.getString("org_id", "").toString();

                        Toast.makeText(UploadToServer.this,etStartTime.getText().toString()+""+org_id, Toast.LENGTH_SHORT).show();*/

                    }

                }
            }
        });
    }

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

    private void updateLabel2()
    {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEndDate.setText(sdf.format(myCalendar.getTime()));
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

    private void updateLabel()
    {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    public int uploadFile(String sourceFileUri)
    {
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;

        SharedPreferences sharedPref    =   getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String path                     =   sharedPref.getString("pathfile", "").toString();
        String name                     =   sharedPref.getString("namefile", "").toString();
        final String uploadFilePath     =   path;
        final String uploadFileName     =   name;

        String org_id                   =   sharedPref.getString("org_id", "").toString();
        String title                    =   etProposalTitle.getText().toString();
        String genObjectives            =   etGenObjectives.getText().toString();
        String specObjectives           =   etSpecObjectives.getText().toString();
        String proposedBudget           =   etBudget.getText().toString();
        String StartDate                =   etStartDate.getText().toString();
        String EndDate                  =   etEndDate.getText().toString();
        String StartTime                =   etStartTime.getText().toString();
        String EndTime                  =   etTimeEnd.getText().toString();
        String ipAddress                =   getString(R.string.ipAddress);
        upLoadServerUri                 =   "http://"+ipAddress+"/ITSQ/omsap_mobile/send_proposal.php?org_id="+org_id+"&title="+title+"&genobjectives="+genObjectives+"&specobjectives="+specObjectives+"&budget="+proposedBudget+"&startdate="+StartDate+"&enddate="+EndDate+"&starttime="+StartTime+"&endtime="+EndTime;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("upLoadServerUri"                       ,   upLoadServerUri);

        String fileName                 =   sourceFileUri;
        HttpURLConnection conn          =   null;
        DataOutputStream dos            =   null;
        String lineEnd                  =   "\r\n";
        String twoHyphens               =   "--";
        String boundary                 =   "*****";
        int maxBufferSize               =   1 * 2024 * 2024;
        File sourceFile                 =   new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            + uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed."
                                    +uploadFileName;

                            messageText.setText(msg);
                            Toast.makeText(UploadToServer.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToServer.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server ", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }









}
