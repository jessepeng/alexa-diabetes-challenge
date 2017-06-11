package de.startupbootcamp.alexachallenge.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NutrionixApiFooodNutritionService implements FoodNutritionService {

    private NutrionixApiFooodNutritionService() {}

    private static class ServiceHolder {
        private static final NutrionixApiFooodNutritionService instance = new NutrionixApiFooodNutritionService();
    }

    public static NutrionixApiFooodNutritionService getService() {
        return ServiceHolder.instance;
    }

	private static final String API_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
	
	@Override
	public double getCarbsInFood(String food) {
		float totalCarbo = 0;	
		String result = this.makeHTTPPOSTRequest(API_URL, food);
		System.out.println("Results:" + result);
		
		JSONParser parser = new JSONParser();
        try
        {
            Object object = parser.parse(result);
            
            //convert Object to JSONObject
            JSONObject jsonObject = (JSONObject)object;
           
            //Reading the array
            JSONArray foods = (JSONArray)jsonObject.get("foods");
        
            System.out.println("Foods:");
            
            String qtdS;
            String carboS;
            float qtd, carbo;
            for(Object line : foods)
            {
            	if (line instanceof JSONObject) {
            		JSONObject foodObject = (JSONObject)line;
            		
            		qtdS = foodObject.get("serving_qty").toString();
            		carboS = foodObject.get("nf_total_carbohydrate").toString();
            		qtd = Float.valueOf(qtdS);
            		carbo = Float.valueOf(carboS);
            		
            		totalCarbo += qtd*carbo;
            		
            		System.out.println("qty: " + qtd + " carbo: " + carbo + " total: " + qtd*carbo );
            	}
            	
	            System.out.println("\tLine: "+line.toString());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Total Carbo: "+totalCarbo);
        
        
		return totalCarbo;
	}
	
	public int getBloodLevel(String user) {
		String api = "http://hackaton.idea-factory.pt/api/sugar/get";
		String result = this.makeHTTPPOSTRequest(api, user);
		
		JSONParser parser = new JSONParser();
        try
        {
            Object object = parser
                    .parse(new String(result));
            
            //convert Object to JSONObject
            JSONObject jsonObject = (JSONObject)object;
            
            
            //Reading the array
            String userName = (String)jsonObject.get("user");
            String sugarValue = (String)jsonObject.get("sugarValue");

            System.out.println("user:" + userName + " glicose value: " + sugarValue);
            return  Integer.valueOf(sugarValue);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0 ; 
	}
	
	private String makeHTTPPOSTRequest(String apiURL, String food) {
		String appID = "677c3426";
		String appKey = "8ed0f5c93b6532010e51d3232c993387";
		String contentType = "application/json";
		String line = "";
		
        try {
            HttpClient c = new DefaultHttpClient();        
            HttpPost p = new HttpPost(apiURL);        
 
            p.setHeader("x-app-id", appID);
            p.setHeader("x-app-key", appKey);
            p.setHeader("Content-Type", contentType);
         
            String xml = "{\"query\":\"" + food + "\",\"timezone\":\"US/Eastern\"}";
            HttpEntity entity = new StringEntity(xml);
            p.setEntity(entity);
         
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
