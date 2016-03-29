package com.twinstar.childtimeutilization;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ChildDBHandler helper = ChildDBHandler.getInstance(this);
     //  helper.emptyTables();
     //   helper.deleteDB(this);

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ChildDBHandler helper = ChildDBHandler.getInstance(this);
        if (helper.isParentTableEmpty().equals(true))
            setMenuItems(navigationView, false);
        else
            setMenuItems(navigationView, true);

    }

    private void setMenuItems(NavigationView navigationView, Boolean b) {
        navigationView.getMenu().getItem(1).setEnabled(b);
        navigationView.getMenu().getItem(2).setEnabled(b);
        navigationView.getMenu().getItem(3).setEnabled(b);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_parent) {
            intent = new Intent(this, ParentInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_child) {
            intent = new Intent(this, AddChildActivity.class);
             startActivity(intent);
        } else if (id == R.id.nav_edit_child) {
            intent = new Intent(this, EditChildActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            intent = new Intent(this, StatisticActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CHORD_QUALITY_REQUEST_CODE) {
//            if(resultCode == Activity.RESULT_OK){
//                String strCurrentChord=data.getStringExtra("strChord");
//                String strCurrentQuality=data.getStringExtra("strQuality");
//                tb1.setChecked(true);
//                setCurrentPlateSet();
//                setCurrentQualityAndPlay(strCurrentChord, strCurrentQuality);
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}
