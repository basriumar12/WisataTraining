package info.blogbasbas.wisatatraining;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.infideap.atomic.Atom;
import com.infideap.atomic.FutureCallback;

import java.util.ArrayList;
import java.util.List;

import info.blogbasbas.wisatatraining.db.facade.Facade;
import info.blogbasbas.wisatatraining.db.facade.ManageWisataTbl;
import info.blogbasbas.wisatatraining.db.model.ResponseWisata;
import info.blogbasbas.wisatatraining.db.model.WisataItem;
import info.blogbasbas.wisatatraining.db.model.WisataItemC;
import info.blogbasbas.wisatatraining.helper.Constant;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<WisataItem> wisataItems;
    ManageWisataTbl manageWisataTbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       //get fragment
       getFragmentHome();

        manageWisataTbl = Facade.getInstance().getManageWisataTbl();
        wisataItems = new ArrayList<>();
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentHome();
                getData();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getFragmentHome() {
        FragmentManager manager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        getSupportActionBar().setTitle(" Home ");
        manager.beginTransaction().replace(R.id.content_main, homeFragment).commit();

    }

    private void getData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Loading Data");
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.show();
        final WisataItemC wisataItem = new WisataItemC();
        Atom.with(MainActivity.this)
                // .load("https://reqres.in/api/users/2")
                // .load("https://wisata-smg-basri.000webhostapp.com/wisata_semarang/wisata_semarang/wisata/read_wisata.php")
                .load(Constant.URL)
                .as(ResponseWisata.class)
                .setCallback(new FutureCallback<ResponseWisata>() {
                    @Override
                    public void onCompleted(Exception e, ResponseWisata result) {
                        if (e != null)
                            Log.e("", "e : " + new Gson().toJson(e));
                        Timber.e("Hasil :"+result);
                        Log.e("", "onCompleted: " + result);

                        if (result.isSuccess()== true){
                            List<WisataItem> wisataItems = result.getWisata();
                            if (wisataItem != null) {
                                manageWisataTbl.removeAll();
                                manageWisataTbl.add(wisataItems);
                            }

                        }
                        mProgressDialog.dismiss();

                    }
                });
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
            finish();
            startActivity(getIntent());

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getFragmentHome();
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
