package de.startupbootcamp.alexachallenge.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NightshiftBodyLevelService implements BodyLevelService {

    protected NightshiftBodyLevelService() {}

    private static class ServiceHolder {
        private static final NightshiftBodyLevelService instance = new NightshiftBodyLevelService();
    }

    public static NightshiftBodyLevelService getService() {
        return ServiceHolder.instance;
    }

	private static final String API_URL = "https://Hackathon2017@alexadiabeteschallenge.azurewebsites.net/api/v1/";

    private static final boolean USE_INSULINE_DECAY = true;
	
	@Override
	public double getGlucoseLevel() {

		String result = this.makeGETRequest(API_URL + "entries");
		
		JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(result);

            //convert Object to JSONObject
            JSONArray array = (JSONArray) object;

            // get first entry
            Object entry = array.get(0);
            JSONObject jsonEntry = (JSONObject)entry;
            long sgv = (long)jsonEntry.get("sgv");

            return sgv / 18.0;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
	public double getActiveInsuline() {
	    if (USE_INSULINE_DECAY) {
            String result = this.makeGETRequest(API_URL + "treatments");

            JSONParser parser = new JSONParser();
            try {
                Object object = parser.parse(result);

                //convert Object to JSONObject
                JSONArray array = (JSONArray) object;

                // get first entry
                if (array.size() > 0) {
                    Object entry = array.get(0);
                    JSONObject jsonEntry = (JSONObject) entry;
                    double insuline = (double) jsonEntry.get("insuline");
                    Long millisecondsIns = Long.valueOf((String) jsonEntry.get("created_at"));
                    Long millisecondsNow = new Date().getTime();

                    long elapsedSeconds = (millisecondsNow - millisecondsIns) / 1000;

                    return (1.0 / (Math.sqrt(2 * Math.PI))) + Math.exp(-(1.0 / 2.0) * Math.pow(elapsedSeconds + 360, 2.0)) * insuline;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
	}
	
	private String makeGETRequest(String apiURL) {
		String contentType = "application/json";
		String line = "";
		
        try {
            HttpClient c = new DefaultHttpClient();
            HttpGet p = new HttpGet(apiURL);
 
            p.setHeader("Content-Type", contentType);
            p.setHeader("Accept", contentType);

            HttpResponse r = c.execute(p);
       
            BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
           
            line = rd.readLine();
            
        }
        catch(IOException e) {
            System.out.println(e);
        }    
        return line;
    }    
}
