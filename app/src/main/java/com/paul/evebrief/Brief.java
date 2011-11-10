package com.paul.evebrief;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class Brief {
    private String publication;
    private String title;
    private String date;
    private String pdfLocation;
    private String imageUrl;
    private com.paul.evebrief.BriefAdapter ba;
    private Bitmap image;

    public Brief(String title, String date, String  pdfLocation, String imageLocation, String publication){
        this.title = title;
        this.date = date;
        this.pdfLocation = pdfLocation;
        this.imageUrl = imageLocation;
        this.publication = publication;
        this.image = null;
    }

  /*  public Bitmap loadImage() {
        Log.i("ImageLoadTask", "Attempting to load image URL: " + imageUrl);
        try {
            //Bitmap b = ImageService.getBitmapFromURLWithScale(param[0]);
            Bitmap b = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());

            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public void loadImage(BriefAdapter ba) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.ba = ba;
        if (imageUrl != null && !imageUrl.equals("")) {
            new ImageLoadTask().execute(imageUrl);
        }
    }
    public Bitmap getImage(){return image;}
    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
    public String getPdfLocation(){
        return pdfLocation;
    }
    public String getImageLocation(){
        return imageUrl;
    }
    public String getPublication(){ return publication;}

    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // param[0] is img url
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                //Bitmap b = ImageService.getBitmapFromURLWithScale(param[0]);
                Bitmap b = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());

                return b;
            } catch (MalformedURLException e) {
                Log.e("ImageLoadTask", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("ImageLoadTask", e.getMessage());
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + title + " image");
                if (ba != null) {
                    Brief.this.image = ret;
                    //imageView.setImageBitmap(ret);
                }
            } else {
               // imageView.setImageBitmap(null);

                Brief.this.image = null;
                Log.e("ImageLoadTask", "Failed to load " + title + " image");
            }

            Brief.this.ba.notifyDataSetChanged();
        }
    }

}
