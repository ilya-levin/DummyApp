package com.example.levin.dummyapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class RequestSenderCallable implements Callable<String> {

    // http://weather.yahooapis.com/forecastrss?w=woeid&u=unit



    private final String mTargetURL;
    private final String mUrlParameters;
    public RequestSenderCallable(String targetURL, String urlParameters) {
        mTargetURL = targetURL;
        mUrlParameters = urlParameters;
    }

    @Override
    public String call() throws Exception {
        return executePost(mTargetURL, mUrlParameters);
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

//    private static Weather parseResponse (String resp, Weather result) {
//        try {
//            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
//            parser.setInput(new StringReader(resp));
//
//            String tagName = null;
//            String currentTag = null;
//
//            int event = parser.getEventType();
//            boolean isFirstDayForecast = true;
//            while (event != XmlPullParser.END_DOCUMENT) {
//                tagName = parser.getName();
//
//                if (event == XmlPullParser.START_TAG) {
//                    if (tagName.equals("yweather:wind")) {
//                    }
//                    else if (tagName.equals("yweather:atmosphere")) {
//                    }
//                    else if (tagName.equals("yweather:forecast")) {
//                    }
//                    else if (tagName.equals("yweather:condition")) {
//                    }
//                    else if (tagName.equals("yweather:units")) {
//                    }
//                    else if (tagName.equals("yweather:location")) {
//                    }
//                    else if (tagName.equals("image"))
//                        currentTag = "image";
//                    else if (tagName.equals("url")) {
//                        if (currentTag == null) {
//                            result.imageUrl = parser.getAttributeValue(null, "src");
//                        }
//                    }
//                    else if (tagName.equals("lastBuildDate")) {
//                        currentTag="update";
//                    }
//                    else if (tagName.equals("yweather:astronomy")) {
//                    }
//
//                }
//                else if (event == XmlPullParser.END_TAG) {
//                    if ("image".equals(currentTag)) {
//                        currentTag = null;
//                    }
//                }
//                else if (event == XmlPullParser.TEXT) {
//                    if ("update".equals(currentTag))
//                        result.lastUpdate = parser.getText();
//                }
//                event = parser.next();
//            }
//
//        }
//        catch(Throwable t) {
//            t.printStackTrace();
//        }
//
//        return result;
//    }

}
