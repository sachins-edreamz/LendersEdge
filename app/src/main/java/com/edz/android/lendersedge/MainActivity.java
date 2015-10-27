package com.edz.android.lendersedge;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.edz.android.lendersedge.Adapter.A_DrawerAdapter;
import com.edz.android.lendersedge.Model.DrawerItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinear;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<DrawerItem> mArrList = new ArrayList<DrawerItem>();
    private DrawerItem mDrwerObj;
    private ImageView imgDrawer;
    private View viewActionBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrwerObj = new DrawerItem("Calculator One", R.drawable.ic_calsi);
        mArrList.add(mDrwerObj);
        mDrwerObj = new DrawerItem("Calculator Two", R.drawable.ic_calsi);
        mArrList.add(mDrwerObj);
        mDrwerObj = new DrawerItem("Calculator Three", R.drawable.ic_calsi);
        mArrList.add(mDrwerObj);
        mDrwerObj = new DrawerItem("Calculator Four", R.drawable.ic_calsi);
        mArrList.add(mDrwerObj);
        mDrwerObj = new DrawerItem("Help", R.drawable.ic_info);
        mArrList.add(mDrwerObj);

        // startService(new Intent(getApplicationContext(),
        // LocationService.class));

        mDrawerList.setAdapter(new A_DrawerAdapter(this, 1, mArrList));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLinear = (LinearLayout) findViewById(R.id.linear_layout);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(
                new ColorDrawable(getResources().getColor(
                        android.R.color.transparent)));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#ffffff")));

        actionbarFirst();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle("");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

	/* Called whenever we call invalidateOptionsMenu() */

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                final int position, long id) {
            // selectItem(position);
            mDrawerLayout.closeDrawer(mDrawerLinear);
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    selectItem(position);

                }
            }, 300);

        }
    }

    @Override
    public void setTitle(CharSequence title) {
        CharSequence mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
       /* if (position == 0) {
            fragment = new FirstCalsiFragment();
            Bundle args = new Bundle();
            args.putInt(HomeFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

        } else if (position == 1) {

            fragment = new SecondCalsiFragment();
            Bundle args = new Bundle();
            args.putInt(HomeFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();


        }*/
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerLinear);

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void actionbarFirst() {


    }

    private void actionBarDrawerClick(int icDrawer) {
        // TODO Auto-generated method stub
        // secondScreen = false;

        imgDrawer = (ImageView) findViewById(R.id.toolbar_left);
        imgDrawer.setImageResource(icDrawer);
        imgDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {

                    mDrawerLayout.closeDrawer(mDrawerLinear);
                } else {

                    mDrawerLayout.openDrawer(mDrawerLinear);
                }

            }
        });

    }

}
