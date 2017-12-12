package com.example.drakewin.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.drakewin.androidlabs.R.id.imageView;
import static com.example.drakewin.androidlabs.R.id.progressBar;

public class WeatherForecast extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView minTextfield;
    private TextView maxTextfield;
    private TextView currtTextfield;
    private ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        // setting the prograss bar to be visible
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // setting up the widgets
        minTextfield = (TextView) findViewById(R.id.minTemperature);
        maxTextfield = (TextView) findViewById(R.id.maxTempture);
        currtTextfield = (TextView) findViewById(R.id.currentTemperature);
        weatherImage =(ImageView) findViewById(R.id.currentWeather);

        ForeCastQuery forecast = new ForeCastQuery();
        forecast.execute();

    }

    public class ForeCastQuery extends AsyncTask<String,Integer,String> {

        private String minTemp;
        private String maxTemp;
        private String currtTemp;
        private Bitmap icon;
        private String ACTIVITY_NAME = "Weather Forecast";
        private String iconName;




        @Override
        protected String doInBackground(String... args) {

            String urlStr = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
            URL url = null;
            HttpURLConnection conn = null;
            InputStream is = null;


            try {
                url = new URL(urlStr);
                //URL url2 = new URL("http//:google.com/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream,null);


                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        currtTemp = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        android.os.SystemClock.sleep(600);
                        minTemp = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        android.os.SystemClock.sleep(600);
                        maxTemp = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                        android.os.SystemClock.sleep(600);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFile = iconName+".png";
                        if (fileExistence(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME, "Image already exists");
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Adding new image");
                        }
                        Log.i(ACTIVITY_NAME, "file name="+iconFile);
                        publishProgress(100);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public  Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responesCode = connection.getResponseCode();
                if (responesCode == 200 ) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(String iconUrl) {
            try {
                URL url = new URL(iconUrl);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer ... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME,"IN onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String Result) {
            progressBar.setVisibility(View.INVISIBLE);
            minTextfield.setText(minTemp);
            maxTextfield.setText(maxTemp);
            currtTextfield.setText(currtTemp);
            weatherImage.setImageBitmap(icon);
        }

    }
    }


