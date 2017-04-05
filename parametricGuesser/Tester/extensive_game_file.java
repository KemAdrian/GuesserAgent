package Tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Guesser.TopXGuesserSsoCountries;
import Tool.Game;
import Tool.MemoryDistance;

/**
 * A Guesser plays a session of the ESSENCE Taboo challenge against a set of predefined clues.
 * The serie comes from a human that was playing the game as a describer. One clue is given to the guesser each turn.
 * You should always play with a {@link TopXGuesserSsoCountries} Guesser.
 * The name of the file with the cities, their clues and taboo words can be changed (line 78).
 * The results are written in an output file. The name of the file can be changed (line 39).
 * @author kemoadrian
 */
public class extensive_game_file {
	
	/**
	 * Starts a session of the ESSENCE Taboo challenge Computer (Serie of recorded clues) vs Computer (Guesser).
	 * @param args an array of {@link String}, with the first string being the {@link Integer} of the number of closest countries to the clue from which the cities will be tested and the second string being the {@link Integer} of the number of most referenced countries from the initial set that will be tested
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// Variables
		int X1 = (Integer.valueOf(args[0]));
		int X2 = (Integer.valueOf(args[1]));
		
		String name_of_file = "Results/";
		
		// Create the file
		if(X2 < 82)
			name_of_file += "C"+X2;
		if(X1 >= 1000)
			name_of_file += "BALL";
		else
			name_of_file += "B"+X1;
		
		
		switch (MemoryDistance.DISTANCE) {
		default:
		case MemoryDistance.WIKIDISTANCE:
			name_of_file += "_WikiDist.csv";
			break;
		case MemoryDistance.BAYESDISTANCE:
			name_of_file += "_firstBayes.csv";
			break;
		case MemoryDistance.JIANCONRATHDISTANCE:
			name_of_file += "_JianCon.csv";
			break;
		case MemoryDistance.PMIDISTANCE:
			name_of_file += "_PMI.csv";
			break;
		}
		
		Path file = Paths.get(name_of_file);
		List<String> lines = new ArrayList<>();
		
		// Variables : possibility to skip some games by entering a game ID and the previous score / total of played games as initial parameters
		MemoryDistance.loadWNDistances("WorldNetDistances");
		MemoryDistance.loadDistances("WikiDistances");
		HashMap<Integer, Game> game_sessions = new HashMap<Integer,Game>();
		Integer initial_total = 0;
		Double initial_score = 0.;
		Integer skip_until = null;
		
		// Open the games data set (standard one)
		BufferedReader br = new BufferedReader(new FileReader("Taboo_cleaned_onlyPerfects.csv"));
		String s = br.readLine();
		while ((s = br.readLine()) != null) {
			if(!s.isEmpty()){
				String[] tokens = s.split(",");
				Integer gameID = Integer.decode(tokens[0]);
				String gameSol = tokens[2];
				String role = tokens[4];
				String clue = tokens[5];
				
				Game g = game_sessions.get(gameID);
				if(g == null)
					game_sessions.put(gameID, new Game(gameID, cleanSolution(gameSol)));
				g = game_sessions.get(gameID);
				if(role.equals("describer") && cleanClue(clue) != null){
					g.addClue(cleanClue(clue));
				}
				
			}
		}
		
		br.close();
		
		// Prepare score : can be loaded from 
		int success = (int) (initial_total * (initial_score / 100.));
		int total = initial_total;
		
		
		// Playing a game session
		boolean skip = true;
		for (Integer i : game_sessions.keySet()) {
			Game current_game = game_sessions.get(i);
			// Skipping the requested games
			if (i.equals(skip_until) || skip_until == null)
				skip = false;
			if (!skip) {
				// Creating a new guesser
				TopXGuesserSsoCountries agent = new TopXGuesserSsoCountries(X1,X2);
				boolean isGood = false;
				// Trying to guess the city by giving the clues incrementally 
				while (current_game.stillHasClue()) {
					String clue = current_game.getNextClue();
					String guess = agent.guess(clue);
					// Compare to answer and eventually increase score
					if (guess.equals(current_game.getSolution())) {
						isGood = true;
						break;
					}
				}
				if (isGood) {
					success++;
				}
				total++;
				// Display formated result
				lines.add("play_id = " + current_game.getID() + " , city_name = " + current_game.getSolution()
						+ " , success = " + isGood + " ; remaining_clues = " + current_game.clues_remaining()
						+ " , average_success = " + (((double) success) / ((double) total)) * 100.);
			}
		}
		MemoryDistance.saveWNDistances("WorldNetDistances");
		MemoryDistance.saveDistances("WikiDistances");
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	/**
	 * Cleans the solution (does not change it in this class).
	 * @param solution a {@link String} with the solution the solution to clean
	 * @return the same {@link String}
	 */
	public static String cleanSolution(String solution){
		return solution;
	}
	
	/**
	 * Put the clue at a format compatible with the Guesser.
	 * @param clue the {@link String} with the clue for the Guesser
	 * @return a {@link String} with the clue at the right format
	 */
	public static String cleanClue(String clue){
		String s = clue.toLowerCase();
		s = s.replaceAll("no", "");
		s = s.replaceAll("[^A-Za-z0-9 ]","");
		s = s.replaceAll("\\.", "");
		s = s.replaceAll("\n", "");
		s = s.replaceAll("\r", "");
		s = s.replaceAll("\t"," ");
		s = s.replaceAll("\\s+"," ");
		if((!s.equals("") || !s.equals("null") || !s.equals(" ") ) && s.length() != 0){
			return s;
		}
		return null;
	}

}
