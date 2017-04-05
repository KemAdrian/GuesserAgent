package Guesser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Tool.MemoryDistance;

/**
 * This class creates a guesser for the ESSENCE Taboo challenge that will, for a given clue, try to guess the corresponging city.
 * It needs a list of cities and to be able to reach https://en.wikipedia.org/.
 * Other classes in the Guesser package allows the experimenter to run tests with different parameters for the guess. See the publication for more information.
 * The TopFive version of the Guesser first find the five countries that have the smallest semantic distances from the clue, and then the city from this country that has the smallest semantic distance.
 * @author kemoadrian
 */
public class TopFiveGuesser {
	
	public static int DEBUG = 0;
	
	private static String countries_and_cities = "cities.tsv";
	
	private Map<String, Set<String>> country_to_city;
	private HashMap<String, Double> CountryScore;
	private HashMap<String, Map<String, Double>> AllCityScores;
	private HashMap<String,Set<String>> countriesClues;
	private HashSet<String> allClues;
	
	/**
	 * Changes the path of the file that references the cities and their information.
	 * @param inputFile a {@link String} with the path that reference the cities and their information
	 */
	public static void setInputFile(String inputFile){
		TopFiveGuesser.countries_and_cities = inputFile;
	}
	
	/**
	 * Create a new instance of {@link Guesser}.
	 * @throws IOException
	 */
	public TopFiveGuesser() throws IOException{
		
		// Local variables
		country_to_city = new HashMap<String,Set<String>>();
		CountryScore = new  HashMap<String, Double>();
		AllCityScores = new  HashMap<String, Map<String, Double>>();
		countriesClues = new HashMap<String,Set<String>>();
		allClues = new HashSet<String>();
		
		if(DEBUG > 0)
			System.out.println("Making map...");
		
		// Initializes the maps that contain countries and cities
		// Open a data-set with countries and cities (Second column are the cities, third column are the countries, no header)
		BufferedReader br = new BufferedReader(new FileReader(countries_and_cities));
		String s;
		while ((s = br.readLine()) != null) {
			if(!s.isEmpty()){
			String[] tokens = s.split("\t"); 
				String city = tokens[1];
				String country = tokens[2];
				Set<String> to_update = country_to_city.get(country);
				if(to_update == null){
					country_to_city.put(country, new HashSet<String>());
				}
				if(DEBUG > 1)
					System.out.println("adding "+city+" to "+country);
				to_update = country_to_city.get(country);
				to_update.add(city);
			}
		}
		
		br.close();
		
		// Use the custom set of city for debug
		/*Set<String> italy_set = new HashSet<String>();
		Set<String> france_set = new HashSet<String>();
		Set<String> australia_set = new HashSet<String>();
		italy_set.add("Roma");
		italy_set.add("Verona");
		italy_set.add("Naples");
		france_set.add("Paris");
		france_set.add("Brest");
		france_set.add("Toulouse");
		australia_set.add("Sydney");
		australia_set.add("Canberra");
		australia_set.add("Melbourne");

		country_to_city.put("Italy", italy_set);
		country_to_city.put("France", france_set);
		country_to_city.put("Australia", australia_set);*/
		
		// Display map creation
		if(DEBUG > 0)
			System.out.println("Map finished!");
		
	}
	
	/**
	 * For a giver clue, return the guessed city.
	 * @param clue A {@link String} with the clue given to the guesser
	 * @return a {@link String} with the name of the guessed city
	 * @throws IOException
	 */
	public String guess(String clue) throws IOException {

		String solution = "";
		allClues.add(clue);
		
		// Starting to look for a country guess
		if (DEBUG > 0)
			System.out.println(" GETTING RIGHT COUNTRY ");
		
		// Display the the provided clue 
		if (DEBUG > 0)
			System.out.println("for the clue " + clue);
		
		// Rank the different countries according to their semantic distance to the clues
		TreeMap<Double, Set<String>> ClueRanked = ranking(clue, country_to_city.keySet());
		// Update the sum of all the previous distances with the new distance
		for (Double score : ClueRanked.keySet()) {
			for (String scoreCountry : ClueRanked.get(score)) {
				if (DEBUG > 0)
					System.out.println("The country " + scoreCountry);
				Double to_update = CountryScore.get(scoreCountry);
				if (to_update == null)
					CountryScore.put(scoreCountry, 0.);
				to_update = CountryScore.get(scoreCountry);
				if (DEBUG > 0) {
					System.out.println("Previously scored " + to_update);
					System.out.println("Now scored " + score);
				}
				to_update += score;
				if (DEBUG > 0)
					System.out.println("The new score is " + to_update);
				CountryScore.put(scoreCountry, to_update);
			}
		}
		
		// Select the 5 countries with the shortest semantic distance to the clue
		Set<String> fiveBestCountries = new HashSet<String>();
		for(Double r : ClueRanked.keySet()){
			boolean stop = false;
			for(String c : ClueRanked.get(r)){
				fiveBestCountries.add(c);
				if(fiveBestCountries.size() > 4){
					stop = true;
					break;
				}
			}
			if(stop)
				break;
		}
		
		// Display ranking for debug
		if (DEBUG > 0)
			System.out.println("Selecting the best country");
		
		
		// Display the guessed countries
		if (DEBUG > 0)
			System.out.println("Best countries are " + fiveBestCountries);
		

		// >>>>>>>>> END OF COUNTRY GUESS - MOVING TO CITY GUESS <<<<<<<<<<
		
		
		// Starting to look for a city guess
		if (DEBUG > 0)
			System.out.println(" GETTING RIGHT CITY ");
		
		// Testing all the countries selected
		TreeMap<Double, Set<String>> FinalRanking = new TreeMap<>();;
		for(String country_solution : fiveBestCountries){
			
			// Getting all the clues already tested with the cities of this country
			Set<String> clues = countriesClues.get(country_solution);
			if (clues == null)
				countriesClues.put(country_solution, new HashSet<>());
			clues = countriesClues.get(country_solution);

			// Getting the previous calculated distances for the cities
			Map<String, Double> CityScore = AllCityScores.get(country_solution);
			if (CityScore == null)
				AllCityScores.put(country_solution, new HashMap<String, Double>());
			CityScore = AllCityScores.get(country_solution);

			// Measuring the distance between the cities and all the clues that hasn't been tested for those cities yet
			for (String c : allClues) {
				if (!clues.contains(c)) {
					// Rank the different cities according to their semantic distance to the clue
					TreeMap<Double, Set<String>> ClueRankedc = ranking(c, country_to_city.get(country_solution));
					for (Double score : ClueRankedc.keySet()) {
						for (String scoreCity : ClueRankedc.get(score)) {
							if (DEBUG > 0) {
								System.out.println("for the clue " + clue);
								System.out.println("The city " + scoreCity);
							}
							Double to_update = CityScore.get(scoreCity);
							if (to_update == null)
								CityScore.put(scoreCity, 0.);
							to_update = CityScore.get(scoreCity);
							if (DEBUG > 0) {
								System.out.println("Previously scored " + to_update);
								System.out.println("Now scored " + score);
							}
							to_update += score;
							if (DEBUG > 0)
								System.out.println("The new score is " + to_update);
							CityScore.put(scoreCity, to_update);
						}
					}
				}
			}
			clues.add(clue);
		}
		
		// Putting the scores of the cities of the five top countries
		for(String country : fiveBestCountries){
			for(String city : country_to_city.get(country)){
				Set<String> to_update =  FinalRanking.get(AllCityScores.get(country).get(city));
				if(to_update == null)
					FinalRanking.put(AllCityScores.get(country).get(city), new HashSet<>());
				to_update =  FinalRanking.get(AllCityScores.get(country).get(city));
				to_update.add(city);
			}
		}
		
		// Select the city with the shortest semantic distance to the clue
		if (DEBUG > 0)
			System.out.println("Selecting the best city");
		solution = randomCountry(FinalRanking.get(bestScore(FinalRanking.keySet())));
		
		// Deleting the city to the set of possible solutions
		for(String country_solution : fiveBestCountries){
			Map<String, Double> CityScore = AllCityScores.get(country_solution);
			country_to_city.get(country_solution).remove(solution);
			CityScore.remove(solution);
			
			// If the country does not have more cities to test, delete it as well
			if (country_to_city.get(country_solution).isEmpty()){
				country_to_city.remove(country_solution);
				CountryScore.remove(country_solution);
			}
		}
		
		if(DEBUG > 0)
			System.out.println("PROPOSAL : "+solution);
		
		// Return solution
		return solution;

	}
	
	/**
	 * Return an ordered list of the different countries and/or cities.
	 * The countries/cities are ranked by their semantic distance to the clue.
	 * The semantic distance is a {@link Double} used as a key for the output {@link TreeMap}.
	 * The {@link String} with the cities/countries names with the same semantic distance are in a {@link Set}, as the entry of each semantic distances.
	 * @param clue a {@link String} with the clue
	 * @param entries a {@link Set} of {@link String} with the name of the cities/countries that need to be ordered
	 * @return a {@link TreeMap} that indexes the cities/countries by their semantic distance to the clue
	 */
	public static TreeMap<Double,Set<String>> ranking(String clue,Set<String> entries){
		if(DEBUG > 0)
			System.out.println("Ordering places");
		TreeMap<Double, Set<String>> to_order = new TreeMap<Double, Set<String>>();
		for (String proposal : entries) {
			Double d = MemoryDistance.distance(proposal, clue);
			Set<String> set = to_order.get(d);
			if(set == null)
				to_order.put(d, new HashSet<String>());
			set = to_order.get(d);
			set.add(proposal);
			if(DEBUG > 0)
				System.out.println("Calculating score of " + proposal + " for clue " + clue+" : get = "+d);
		}
		return to_order;
	}
	
	/**
	 * Return the smallest value of a set of doubles.
	 * @param scores a {@link Set} of double which of the minimal value is desired
	 * @return the minimal value of a set of {@link Double}
	 */
	private static Double bestScore(Set<Double> scores){
		Double min = Double.MAX_VALUE;
		for(Double s : scores){
			if(s<min){
				min = s;
			}
		}
		return min;
	}
	
	/**
	 * Return a {@link String} with the name of a random country from a given {@link Set} of country names.
	 * @param countries a {@link Set} of {@link String} that are the name of the countries
	 * @return the {@link String} with the name of a random country
	 */
	private static String randomCountry(Set<String> countries){
		Double r = Math.floor(Math.random() * countries.size());
		Double c = 0.;
		for(String s : countries){
			if(r.doubleValue() == c.doubleValue())
				return s;
			c += 1.;
		}
		return null;
	}
	
}
