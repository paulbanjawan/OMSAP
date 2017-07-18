package com.example.pcban.omsap;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pcban.omsap.EquipmentReservation.EquipmentReservationStep1;
import com.example.pcban.omsap.MyReservations.MyReservedRooms;
import com.example.pcban.omsap.OrgProfile.ChangePassword;
import com.example.pcban.omsap.OrgProfile.editProfile;
import com.example.pcban.omsap.OrgProfile.fragmentMyProfile;
import com.example.pcban.omsap.OrgProposals.fragMyProposals;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SessionManager session;
    TextView OrgName,OrgAbbrev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fm = getFragmentManager();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            FragmentManager fm = getFragmentManager();

            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.main,new Uploading_page()).commit();

*//*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*//*
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        OrgAbbrev = (TextView)header.findViewById(R.id.tvHeaderOrgAbbrev);
        OrgName = (TextView)header.findViewById(R.id.tvHeaderOrgName);
        OrgAbbrev.setText(sharedPref.getString("organization_abbreviation",""));
        OrgName.setText(sharedPref.getString("organization_name",""));
        fm.beginTransaction().replace(R.id.main,new fragMyProposals()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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
            Intent ii = new Intent(MainActivity.this, editProfile.class);
            startActivity(ii);
            return true;
        }
        if (id == R.id.change_password) {
            Intent ii = new Intent(MainActivity.this, ChangePassword.class);
            startActivity(ii);
            return true;
        }
        if (id == R.id.logout) {
            session.logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        FragmentManager fm = getFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile)
        {
            fm.beginTransaction().replace(R.id.main,new fragmentMyProfile()).commit();
        }
        else if (id == R.id.nav_myproposals)
        {
            fm.beginTransaction().replace(R.id.main,new fragMyProposals()).commit();
        }
      /*  else if (id == R.id.nav_myreservations)
        {
            fm.beginTransaction().replace(R.id.main,new MyReservedRooms()).commit();
        }*/
        else if (id == R.id.nav_eqreservations)
        {
            fm.beginTransaction().replace(R.id.main,new EquipmentReservationStep1()).commit();
        }

       /* else if (id == R.id.nav_reservations)
        {
            fm.beginTransaction().replace(R.id.main,new RoomReservationStep1()).commit();

        }*/
        else
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
