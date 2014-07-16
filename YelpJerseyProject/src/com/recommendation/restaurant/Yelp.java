package com.recommendation.restaurant;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import org.apache.log4j.Logger;

@Path("restaurant_data/")

public class Yelp extends Application{
	

	OAuthService service;
	Token accessToken;
	
	private static Logger log = Logger.getLogger(Yelp.class.getName());

  
  // http://localhost:5050/YelpJerseyProject/rest/restaurant_data/restaurant/?term=food&latitude=46.621&longitude=-122.3331&radius=40000
  
  public static String getRestaurantSearched(Response recievedResponse) {
	  
	  JSONArray finalResponse=new JSONArray();
	  JSONObject json;
	  
		try {
			String rawResponse = recievedResponse.getBody();
			json =  new JSONObject(rawResponse);
			JSONArray restaurantArray = json.getJSONArray("businesses");
			
			for (int i = 0; i < restaurantArray.length(); i++) {
				JSONObject restaurants = restaurantArray.getJSONObject(i);
				String[] keys = {"name", "image_url", "rating_img_url_small", "rating_img_url_large", "mobile_url", "rating",  "phone"};
				JSONObject filteredRestaurants;
				filteredRestaurants = Utility.filterJSONResponse(restaurants, keys);
				
			    finalResponse.put(filteredRestaurants);
				log.debug("Parsed the response from the server, Incident fetched");
			}
		} catch (JSONException e) {
			log.debug("ERROR: Cannot parse the JSON response receieved from server", e);
		}
		return finalResponse.toString();
	}
  
  @GET
	
  @Path("restaurant/")
	
  @Produces(MediaType.TEXT_PLAIN)
  
  public String search(@QueryParam("term") String term, @QueryParam("latitude") String latitude, @QueryParam("longitude") String longitude, @QueryParam("radius") String radius) {
	  
	  String consumerKey = "xxxxxxxxxxxxxxxxxxxxxx";
	  String consumerSecret = "xxxxxxxxxxxxxxxxxxxxxx";
	  String token = "xxxxxxxxxxxxxxxxxxxxxx";
	  String tokenSecret = "xxxxxxxxxxxxxxxxxxxxxx";
	  this.service = new ServiceBuilder().provider(YelpV2API.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
	  this.accessToken = new Token(token, tokenSecret);
	  
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("radius_filter_meters", radius);
    request.addQuerystringParameter("sort", ""+1);
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    
    String res = getRestaurantSearched(response);
    return res;
  }
  
  // CLI
  public static void main(String[] args) {
		Yelp yelp = new Yelp();
		System.out.println(yelp.search("burritos", "47", "-122", "50000"));
	}
}