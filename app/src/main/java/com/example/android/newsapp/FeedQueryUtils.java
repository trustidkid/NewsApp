package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FeedQueryUtils {

    public static final String LOG_TAG = FeedQueryUtils.class.getName();
    /**
     * Create a private constructor so that no one should try to create a {@link FeedQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private FeedQueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchEarthquakeData(String requestUrl) {

        //to show the progress bar on the UI we decided to delay execution
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News Article}
        List<News> newsList = extractFeatureEarthquakes(jsonResponse);

        // Return the list of {@link News}s
        return newsList;
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<News> extractFeatureEarthquakes(String newsFeedJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsFeedJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<News> newsFeed = new ArrayList<>();

        // Try to parse the NEWS_FEED_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the NEWS_FEED_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            //create JSONObject
            JSONObject jsonRootObject = new JSONObject(newsFeedJSON);

            JSONObject jsonResponseKey = jsonRootObject.getJSONObject("response");
            //get the instance of JSON Array that contain JSON Object
            JSONArray jsonArray = jsonResponseKey.optJSONArray("results");

            for (int i = 0; i < 2; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //JSONObject resultsKey = jsonObject.optJSONObject("results");

                String title = jsonObject.getString("webTitle");
                String sectionName = jsonObject.getString("sectionName");
                String publishDate = jsonObject.getString("webPublicationDate");
             //   String author = "";

//                if(jsonObject.getString("webauthor") ==null)
//                {
//                    author = jsonObject.getString("");
//                }
                //else author = jsonObject.getString("webauthor");

                String url = jsonObject.getString("webUrl");

//                long milliseconds = Long.parseLong(time.toString());
//                Date date = new Date(milliseconds);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD,yyyy HH:mm:ss");
//                String earthquakeTime = dateFormat.format(date);
                Log.i(LOG_TAG,"RETURN VALUE"+ sectionName+" ,"+title +"url");
                News quake = new News(sectionName,publishDate,title,url);
                newsFeed.add(quake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("FeedQueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return newsFeed;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
