package Tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import Guesser.TopXGuesser;
import Tool.Game;
import Tool.MemoryDistance;

/**
 * A Guesser plays a session of the ESSENCE Taboo challenge against a serie of predefined clues.
 * Unlike {@link extensive_game} and {@link extensive_game_file}, the game does not end when no clue remains.
 * The game continues until the Guesser successes to find the right city.
 * The serie comes from a human that was playing the game as a describer. One clue is given to the guesser each turn.
 * The class of Guesser can me modified (line 74).
 * The name of the file with the cities, their clues and taboo words can be modified (line 37).
 * @author kemoadrian
 */
public class until_success_game {
	
	/**
	 * Starts a session of the ESSENCE Taboo challenge Computer (Serie of recorded clues) vs Computer (Guesser).
	 * @param args no argument needed
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// Variables : possibility to skip some games by entering a game ID and the previous score / total of played games as initial parameters
		MemoryDistance.loadDistances("WikiDistances");
		HashMap<Integer, Game> game_sessions = new HashMap<Integer,Game>();
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
		
		// Playing a game session
		boolean skip = true;
		for (Integer i : game_sessions.keySet()) {
			Game current_game = game_sessions.get(i);
			Integer total_try = 0;
			// Skipping the requested games
			if (i.equals(skip_until) || skip_until == null)
				skip = false;
			if (!skip) {
				// Creating a new guesser
				TopXGuesser agent = new TopXGuesser(1000);
				boolean isGood = false;
				// Trying to guess the city by giving the clues incrementally 
				String clue = "";
				while (!isGood) {
					if(current_game.stillHasClue())
						clue = current_game.getNextClue();
					String guess = agent.guess(clue);
					// Compare to answer and eventually increase score
					if (guess.equals(current_game.getSolution())) {
						isGood = true;
						break;
					}
					total_try ++;
				}
				// Display formated result
				System.out.println("play_id = " + current_game.getID() + " ; city_name = " + current_game.getSolution()
						+ " ; success = " + isGood + " ; remaining_clues = " + current_game.clues_remaining() + " ; total_tries = "+ total_try);
			}
		}
		MemoryDistance.saveDistances("WikiDistances");
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
		if((!s.equals("") || !s.equals("null") || !s.equals(" ") ) && s.length() > 1){
			return s;
		}
		return null;
	}

}
