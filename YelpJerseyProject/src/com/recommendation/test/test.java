package com.recommendation.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.recommendation.restaurant.*;

import static org.junit.Assert.*;

public class test {
	
	@Test
	public void testJSONResponeIsNotNull() {
		Yelp yelp = new Yelp();
		String JSONResponse = yelp.search("food", "47.621", "-122.3331", "40000");
		assertNotNull("JSON Response cannot be null.", JSONResponse);
		assertNotSame("JSON Response cannot be empty.", "", JSONResponse);
	}
	
	@Test
	public void testYelpApiHasRequiredFields() throws JSONException {
		Yelp yelp = new Yelp();
		String stringResponse = yelp.search("food", "47.621", "-122.3331", "40000");
		JSONArray responseArray = new JSONArray(stringResponse);
		
		for (int i = 0; i < responseArray.length(); i++) {
			try {
				JSONObject restaurant = responseArray.getJSONObject(i);
				assertTrue("An event object does not specify \'channel\' field.", restaurant.has("name"));
				assertTrue("An event object does not specify \'description\' field.", restaurant.has("image_url"));
				assertTrue("An event object does not specify \'minPrice\' field.", restaurant.has("rating_img_url_small"));
				assertTrue("An event object does not specify \'venue_name\' field.", restaurant.has("rating_img_url_large"));
				assertTrue("An event object does not specify \'lat_lon\' field.", restaurant.has("mobile_url"));
				assertTrue("An event object does not specify \'timezone\' field.", restaurant.has("rating"));
				assertTrue("An event object does not specify \'addr1\' field.", restaurant.has("phone"));
			} catch (JSONException e) {
				fail("Cannot get events as JSON objects.");
			}
		}
	}

}
