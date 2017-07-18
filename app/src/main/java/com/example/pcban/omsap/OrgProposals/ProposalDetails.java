package com.example.pcban.omsap.OrgProposals;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.pcban.omsap.MainActivity;
import com.example.pcban.omsap.MyReservations.MyReservedRooms;
import com.example.pcban.omsap.OrgProfile.ChangePassword;
import com.example.pcban.omsap.OrgProfile.editProfile;
import com.example.pcban.omsap.R;
import com.example.pcban.omsap.SessionManager;

public class ProposalDetails extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_details);
//        new getProposalInfo().execute();
//        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proposal_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        session = new SessionManager(getApplicationContext());

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_profile) {
            Intent ii = new Intent(ProposalDetails.this, editProfile.class);
            startActivity(ii);
            return true;
        }
        if (id == R.id.change_password) {
            Intent ii = new Intent(ProposalDetails.this, ChangePassword.class);
            startActivity(ii);
            return true;
        }
        if (id == R.id.logout) {
            session.logoutUser();
        }


        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    Tab1ProposalInfo tab1 = new Tab1ProposalInfo();
                    return tab1;
                case 1:
                    Tab2Status tab2 = new Tab2Status();
                    return tab2;
                case 2:
                    Tab3Comments tab3 = new Tab3Comments();
                    return tab3;
                case 3:
                    Tab4MyReservedRooms tab4 = new Tab4MyReservedRooms();
                    return tab4;
                case 4:
                    Tab5EquipmentReservation tab5 = new Tab5EquipmentReservation();
                    return tab5;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Proposal Information";
                case 1:
                    return "Status";
                case 2:
                    return "Comments";
                case 3:
                    return "Room Reservations";
                case 4:
                    return "Equipment Reservations";

            }
            return null;
        }
    }

}
