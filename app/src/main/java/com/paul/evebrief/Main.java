package com.paul.evebrief;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends ActionBarActivity {

    private int noDisplayed = 0;
    private DisplayedBriefs displayedBriefs;
    private ListView mDrawerList;
    private String briefSelected = "EveBrief";
    private ArrayAdapter<String> mDrawerAdapter;
    private CharSequence mTitle;
    private ListView lv;
    private ArrayList<Brief> briefs;
    private BriefAdapter lvadapter;
    private ArrayList<String> titles;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle = "EveBrief";
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        initialiseBriefPage();
    }
    private void createNavigationDrawer(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView)findViewById(R.id.navList);
        String[] briefArray = {"EveBrief", "InvBrief", "Engage", "Rating News Update"};
        mDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, briefArray);
        mDrawerList.setAdapter(mDrawerAdapter);

        mDrawerList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView clicked = (TextView) view;
                String selectedBrief = ""+clicked.getText();
                mToolbar.setTitle(clicked.getText());
                //Toast.makeText(Main.this, "You Clicked : " + selectedBrief, Toast.LENGTH_SHORT).show();
                briefSelected = selectedBrief;
                try {
                    briefs = EveDataExtractor.extractEveBriefData(selectedBrief);
                    displayedBriefs = new DisplayedBriefs();
                    noDisplayed = 0;

                    while(noDisplayed < briefs.size() && noDisplayed < 5) {
                        displayedBriefs.add(briefs.get(noDisplayed), lvadapter);
                        noDisplayed++;
                    }

                    lvadapter.setBriefs(displayedBriefs);
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                lvadapter.notifyDataSetChanged();
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void initialiseBriefPage() {
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main2);


        createNavigationDrawer();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            briefs = EveDataExtractor.extractEveBriefData(briefSelected);
            if(briefs == null || briefs.size() < 1) {
                Toast.makeText(Main.this, "no Briefs where found", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        lv = (ListView) findViewById(R.id.listView);


        displayedBriefs = new DisplayedBriefs();
        lvadapter = new BriefAdapter(getApplicationContext(),displayedBriefs);

        lvadapter.setBriefs(displayedBriefs);
        View lmView = View.inflate(this,R.layout.load_more_button,null);

        while(noDisplayed < briefs.size() && noDisplayed < 5) {
            displayedBriefs.add(briefs.get(noDisplayed), lvadapter);
            noDisplayed++;
        }
        lv.addFooterView(lmView);
        lv.setAdapter(lvadapter);


        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Intent appInfo = new Intent(Main.this, Eve_PDF_Viewer.class);
                if(position < displayedBriefs.size()) {
                    appInfo.putExtra("PDF_Location", briefs.get(position).getPdfLocation());
                    startActivity(appInfo);
                }else {
                    while(noDisplayed < briefs.size() && noDisplayed < position + 5) {
                        displayedBriefs.add(briefs.get(noDisplayed), lvadapter);
                        noDisplayed++;
                    }

                    lvadapter.setBriefs(displayedBriefs);
                    lvadapter.notifyDataSetChanged();

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
