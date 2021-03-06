package com.paul.evebrief;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class Eve_PDF_Viewer extends Activity {
    private String pdfLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pdfLocation = extras.getString("PDF_Location");
        }
        setContentView(R.layout.activity_eve_pdf_viewer);

        //settings for the web view to make it pdf friendly
        WebView mWebView=new WebView(Eve_PDF_Viewer.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        //google's url that automatically displays pdf files on the internet in a web view.
        mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" +pdfLocation);

        setContentView(mWebView);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
