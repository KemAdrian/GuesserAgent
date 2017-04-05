package Tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wikipedia.WikipediaRequest;
import wordNet.WordNetRequest;

/**
 * {@link MemoryDistance} regroups the methods related to semantic distances.
 * This class computes and saves the semantic distances between cities and clues.
 * The saved distances are recorded in a file and eventually reopened.
 * @author kemoadrian
 *
 */
public class MemoryDistance {
	
	// 4 possibles distances available (BAYESDISTANCE is called probabilistic in the publication)
	public static final int WIKIDISTANCE = 0;
	public static final int BAYESDISTANCE = 1;
	public static final int JIANCONRATHDISTANCE = 2;
	public static final int PMIDISTANCE = 3;
	
	// Distance that will be used to return results to other classes
	public static int DISTANCE = BAYESDISTANCE;
	
	// Number of hints for the query "United" States (largest number of hits for a country)
	private static double N = 5085505.; 
	private static Map<String, Double> memory = new HashMap<String, Double>();
	private static Map<String, Double> wn_memory = new HashMap<String, Double>();
	private static WordNetRequest worldNetRessource = new WordNetRequest();

	/**
	 * Sets the maximum value for the number of hits that a query can have (usually a really common query).
	 * Only used for the distances based on wikipedia queries.
	 * @param N a {@link Double} to set as the maximum value
	 */
	public static void setN(Double N) {
		MemoryDistance.N = N;
	}
	
	/**
	 * Number of hits for a {@link String} query in Wikipedia.
	 * If this query has already been tested, it is loaded from the memory file.
	 * @param s the {@link String} with the query
	 * @return the {@link Double} with the number of hits
	 */
	public static Double nbHints(String s){
		Double o = memory.get(s);
		if(o == null){
			ArrayList<String> to_test = new ArrayList<String>();
			to_test.add(s);
			o = Double.valueOf(WikipediaRequest.getHits(to_test));
		}
		return o;
	}

	/**
	 * Gives the semantic distance between a country and a clue.
	 * The value will depend of the semantic distance selected as a static {@link Integer} of the class.
	 * @param country a {@link String} with the name of country 
	 * @param clue a {@link String} with the name of the clue
	 * @return a {@link Double} with the semantic distance between the country and the clue
	 */
	public static Double distance(String country, String clue) {
		
		double s = 1.;
		// Special case of the Bayesian distance (Wordnet)
		if(DISTANCE == 2){
			if (wn_memory.get(country + "+" + clue) == null) {
				if (wn_memory.get(clue + "+" + country) == null) {
					wn_memory.put(country + "+" + clue, worldNetRessource.distance(country, clue));
					s = wn_memory.get(country + "+" + clue);
				} else {
					s = wn_memory.get(clue + "+" + country);
				}
			} else {
				s = wn_memory.get(country + "+" + clue);
			}
			return s;
		}
		// Otherwise, check the distances normally
		else{
			ArrayList<String> to_test = new ArrayList<String>();
			to_test.add(country);
			if (memory.get(country) == null) {
				memory.put(country, Double.valueOf(WikipediaRequest.getHits(to_test)));
			}
			double x = memory.get(country);
			to_test.add(clue);
			double xy = 0.;
			if (memory.get(country + "+" + clue) == null) {
				if (memory.get(clue + "+" + country) == null) {
					memory.put(country + "+" + clue, Double.valueOf(WikipediaRequest.getHits(to_test)));
					xy = memory.get(country + "+" + clue);
				} else {
					xy = memory.get(clue + "+" + country);
				}
			} else {
				xy = memory.get(country + "+" + clue);
			}
			to_test.remove(country);
			if (memory.get(clue) == null) {
				memory.put(clue, Double.valueOf(WikipediaRequest.getHits(to_test)));
			}
			double y = memory.get(clue);
			double hitsX = Math.log(Math.max(x, 1));
			double hitsY = Math.log(Math.max(y, 1));
			double hitsXY = Math.log(Math.max(xy, 1));

			double nom = 1.;
			double denom = 1.;

			// Calculate the distance
			switch (DISTANCE) {
			default:
			case 0:
				nom = Math.max(hitsX, hitsY) - hitsXY;
				denom = Math.log(N) - Math.min(hitsX, hitsY);
				s = nom / denom;
				break;
			case 1:
				nom = Math.log(xy);
				denom = Math.log(x);
				s = 1 - (nom / denom);
				break;
			case 3:
				s = - Math.log((xy / y) / (x / N));
			}
		}

		if (s == Double.NaN || !Double.isFinite(s))
			return N;
		return s;

	}
	
	/**
	 * Save the Wikipedia distances in a file.
	 * @param file the {@link String} with the path and name of the file
	 */
	public static void saveDistances(String file){
		//System.out.println("Saving distances...");
		try {
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(memory);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Distances saved!");
	}
	
	/**
	 * Save the wordnet distances in a file.
	 * @param file the {@link String} with the path and name of the file
	 */
	public static void saveWNDistances(String file){
		//System.out.println("Saving distances...");
		try {
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(wn_memory);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Distances saved!");
	}
	
	/**
	 * Load the Wikipedia distances from a file.
	 * @param file the {@link String} with the path and name of the file
	 */
	@SuppressWarnings("unchecked")
	public static void loadDistances(String file){
		//System.out.println("Loading distances...");
		try {
		FileInputStream fin = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fin);   
			memory = (Map<String, Double>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("Data-base loaded!");
	}
	
	/**
	 * Load the wordnet distances from a file.
	 * @param file the {@link String} with the path and name of the file
	 */
	@SuppressWarnings("unchecked")
	public static void loadWNDistances(String file){
		//System.out.println("Loading distances...");
		try {
		FileInputStream fin = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fin);   
			wn_memory = (Map<String, Double>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("Data-base loaded!");
	}

}
