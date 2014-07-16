package com.recommendation.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
	
	public static JSONObject filterJSONResponse(JSONObject obj, String[] keyFilters) throws JSONException {
		
		JSONObject filteredJSON = new JSONObject();
		
		JSONObject locationJson = obj.getJSONObject("location");
		filteredJSON.put("city", locationJson.get("city"));
		filteredJSON.put("state", locationJson.get("state_code"));
		filteredJSON.put("zip", locationJson.get("postal_code"));
		
		JSONArray addressArray = locationJson.getJSONArray("address");
		filteredJSON.put("address", addressArray.getString(0));
		
		JSONArray cuisineArray = obj.getJSONArray("categories");
		JSONArray nameArray = new JSONArray();
		JSONArray tempArray = new JSONArray();
		
		for (int k = 0; k < cuisineArray.length(); k++) {
			
			tempArray = cuisineArray.getJSONArray(k);
			
			nameArray.put(tempArray.get(0));
		}
		filteredJSON.put("cuisines", nameArray);
		
		for (int i = 0; i < keyFilters.length; i++) {
			filteredJSON.put(keyFilters[i], obj.get(keyFilters[i]));
		}
		return filteredJSON;
	}
}
