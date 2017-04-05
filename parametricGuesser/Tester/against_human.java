package Tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import Guesser.TopXGuesserSsoCountries;
import Tool.HumanGame;
import Tool.MemoryDistance;

/**
 * A Guesser plays a session of the ESSENCE Taboo challenge against a human.
 * The human is the describer. He/She has to give a clue to the Guesser each turn.
 * The class of Guesser can be changed (line 62).
 * The name of the file with the cities and their taboo words can be changed (line 35).
 * @author kemoadrian
 */
public class against_human {

	/**
	 * Starts a session of the ESSENCE Taboo challenge Human (Describer) vs Computer (Guesser).
	 * @param args no argument needed
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		HashMap<Integer, HumanGame> game_sessions = new HashMap<Integer,HumanGame>();

		// Getting Input
		Scanner keyboard = new Scanner(System.in);
		
		// Open the games data set (standard one)
		BufferedReader br = new BufferedReader(new FileReader("Taboo_cleaned_onlyPerfects.csv"));
		String s = br.readLine();
		int i = 0;
		while ((s = br.readLine()) != null) {
			if(!s.isEmpty()){
				String[] tokens = s.split(",");
				Integer gameID = Integer.decode(tokens[0]);
				String gameSol = tokens[2];
				String role = tokens[4];
				String clue = tokens[5];
				
				HumanGame g = game_sessions.get(gameID);
				if(g == null)
					game_sessions.put(i, new HumanGame(gameID, cleanSolution(gameSol)));
				g = game_sessions.get(i);
				g.setTaboos(tokens[6]);
				if(role.equals("describer") && cleanClue(clue) != null){
					g.addClue(cleanClue(clue));
				}
				
			}
			i++;
		}
		
		// Playing game against computer
		MemoryDistance.loadDistances("WikiDistances");
		while(!game_sessions.isEmpty()){
			TopXGuesserSsoCountries agent = new TopXGuesserSsoCountries(15, 50);
			Integer randomGameSession = (int) (Math.random() * (game_sessions.size() - 1));
			HumanGame g = game_sessions.get(randomGameSession);
			boolean isGood = false;
			while (!isGood) {
				System.out.println("You have to make the computer guess " + g.getSolution());
				System.out.println("You cannot use the words (or derivated) : " + g.displayTaboo());
				String hint = keyboard.nextLine();
				while (hint == null || g.isTaboo(hint)) {
					System.out.println("please, again");
					hint = keyboard.nextLine();
				}
				String guess = agent.guess(hint);
				if (guess.equals(g.getSolution())){
					System.out.println("Congratulation! Next game!");
					isGood = true;
					game_sessions.remove(randomGameSession);
				}
				else
					System.out.println("the computer thinks that you mean "+guess);
			}
		}
		
		br.close();
		keyboard.close();
		
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
