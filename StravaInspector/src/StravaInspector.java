import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

public class StravaInspector {
	
	static String ClientId = "21741";
	static String ClientSecret = "5ecadbdfb5b6367f3118c892a34afddb3af8a49a";
	static String RefreshTokenActivityReadAll = "e4f590413bfa62d366e74e9a42afd5e99d15d1c3";	
	static String RefreshTokenProfileReadAll = "b8c170b4526265f16bc273b67f4a059ff60d0d69";
	
	static final String UrlBase = "https://www.strava.com/api/v3/";
	static final String UrlActivity = UrlBase + "activities/";
	static final String UrlAthlete = UrlBase + "athlete";
	
	
	static String AccessToken;
	static HttpURLConnection con;
	static URL url;

	public static void main(String[] args) throws IOException 
	{
		
		AccessToken = GetToken(RefreshTokenActivityReadAll);				
		String ActivityId = "3429474814"; 		
		url = new URL(UrlActivity + ActivityId + "?access_token=" + AccessToken );		
		GetRequest();
        System.out.println(GetResponse());                        
        con.disconnect();
        
        
		AccessToken = GetToken(RefreshTokenProfileReadAll);
		url = new URL(UrlAthlete + "?access_token=" + AccessToken );		
		GetRequest();
        System.out.println(GetResponse());                        
        con.disconnect();


	}
	
	public static String GetToken(String RefreshToken) throws IOException
	{			
		url = new URL("https://www.strava.com/oauth/token?grant_type=refresh_token&client_id=" + ClientId + "&client_secret=" + ClientSecret + "&refresh_token=" + RefreshToken);			
		PostRequest();
		
	    Gson gson = new Gson();
	    Token token = gson.fromJson(GetResponse(), Token.class);
	
	    con.disconnect();
	        
	    return token.getAccess_token();
	}
	
	public static String GetResponse() throws UnsupportedEncodingException, IOException
	{
		StringBuilder response;
		
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) 
        {
        		    response = new StringBuilder();
        		    String responseLine = null;
        		    while ((responseLine = br.readLine()) != null) 
        		    {
        		        response.append(responseLine.trim());
        		    }
        }

        return response.toString();
	}
	
	public static void GetRequest() throws IOException
	{
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.connect();    
	}

	public static void PostRequest() throws IOException
	{
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();    
	}

}
