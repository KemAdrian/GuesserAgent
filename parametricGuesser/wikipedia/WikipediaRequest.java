package wikipedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Manage the queries sent to Wikipedia and the answers received.
 * @author kemoadrian
 *
 */
public class WikipediaRequest {

	/**
	 * Get the number of hits that a list of queries have on Wikipedia.
	 * @param query the {@link List} of {@link String}, with each string being a query
	 * @return the {@link Integer} cumulated number of hits that the queries made
	 */
	public static Integer getHits(List<String> query) {
		if(query.isEmpty())
			return null;
		String search = query.get(0).replace(" ", "%20");
		for(int i=1;i<query.size();i++)
			search += ("+"+query.get(i).replace(" ", "%20"));
		//System.out.println(search);
	    String wikidata = "https://en.wikipedia.org/w/api.php?action=query&maxlag=30&format=json&list=search&srsearch="+search+"&srnamespace=0&srwhat=text&srinfo=totalhits";
	    String line = null;
	    WikiResult result = null;
	    URL url;
		try {
			url = new URL(wikidata);
	    URLConnection con = url.openConnection();
		InputStream is = con.getInputStream();
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    line = reader.readLine();
	    result = new Gson().fromJson(line, WikiResult.class);
	    
		} catch (MalformedURLException e) {
			System.out.println(search);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(search);
			e.printStackTrace();
		}
		
		if(result.totalHit() == null)
			System.out.println(line);
		
		Integer s = result.totalHit();
	    return s;
	}
	
	/**
	 * Get the number of hits that a query has on Wikipedia.
	 * @param query a {@link String} with the query
	 * @return
	 */
	public static Integer getHits(String query) {
		if(query.isEmpty())
			return null;
		String search = query.replace(" ", "%20");
		//System.out.println(search);
	    String wikidata = "https://en.wikipedia.org/w/api.php?action=query&maxlag=30&format=json&list=search&srsearch="+search+"&srnamespace=0&srwhat=text&srinfo=totalhits";
	    String line = null;
	    WikiResult result = null;
	    URL url;
		try {
			url = new URL(wikidata);
	    URLConnection con = url.openConnection();
		InputStream is = con.getInputStream();
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    line = reader.readLine();
	    System.out.println(line);
	    result = new Gson().fromJson(line, WikiResult.class);
	    
		} catch (MalformedURLException e) {
			System.out.println(search);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(search);
			e.printStackTrace();
		}
		
		if(result.totalHit() == null)
			System.out.println(line);
		
		Integer s = result.totalHit();
	    return s;
	}
	
	/**
	 * Get the text from the answer of a query result.
	 * @param query a {@link String} with the raw answer of the query
	 * @return a {@link List} of {@link String}, each string being a word of the cleaned result
	 */
	public static List<String> getText(String query){
		if(query.isEmpty())
			return null;
		String search = query.replace(" ", "%20");
		String wikidata = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&indexpageids=1&titles="+search+"&explaintext=1";
		String line = null;
		WikiTextResult result = null;
		URL url;
		
		try {
			url = new URL(wikidata);
	    URLConnection con = url.openConnection();
		InputStream is = con.getInputStream();
		
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    line = reader.readLine();
	    line = line.replaceAll("pages\":\\{\".*\":\\{\"pageid", "pages\":{\"page\":{\"pageid");
	    result = new Gson().fromJson(line, WikiTextResult.class);
	    
		} catch (MalformedURLException e) {
			System.out.println(search);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(search);
			e.printStackTrace();
		}
		
		if(result.text() == null)
			System.out.println(line);
		String s = result.text().toLowerCase();
		s = s.replaceAll("[^A-Za-z0-9 ]"," ");
		s = s.replaceAll("\\.\\,\\;", " ");
		s = s.replaceAll("\n", " ");
		s = s.replaceAll("\r", " ");
		s = s.replaceAll("\t"," ");
		s = s.replaceAll("\\s+"," ");
		String[] tokens = s.split(" ");
		ArrayList<String> output =  new ArrayList<>();
		for(int i=0; i<tokens.length; i++){
			output.add(tokens[i]);
		}
		
		//System.out.println(result.text());

		return output;
	}
	
}
