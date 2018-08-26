package com.example.mahdi.simplewebview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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

    private WebViewFragment curWebFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        toolbar.setSubtitle(menuItem.getTitle());

        curWebFragment = WebViewFragment.getInstance(menuItem.getTitleCondensed().toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.content, curWebFragment).commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (curWebFragment != null && curWebFragment.canGoBack()) {
            return;
        }
        super.onBackPressed();

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (webViewFragment != null) {
//            return webViewFragment.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
        if (id == R.id.action_refresh) {
            curWebFragment.retryLoad();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String api = item.getTitleCondensed().toString();
        if (id == R.id.nav_git_doc) {
//            api = API.git_doc;
        } else if (id == R.id.nav_kotlin) {
//            api = API.kotlin_doc;
        } else if (id == R.id.nav_min_program) {
//            api = API.mini_program;
        } else if (id == R.id.nav_design_pattern) {
//            api = API.design_pattern;
        } else if (id == R.id.nav_android_interview) {
//            api = API.android_interview;
        } else if (id == R.id.nav_android_gradle) {
//            api = API.android_gradle;
        } else if (id == R.id.nav_gank_io) {
//            api = API.gank_io;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (!TextUtils.isEmpty(api) && !api.equals(curWebFragment.getCurWebApi())) {
            curWebFragment = WebViewFragment.getInstance(api);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, curWebFragment).commitAllowingStateLoss();
            toolbar.setSubtitle(item.getTitle());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
