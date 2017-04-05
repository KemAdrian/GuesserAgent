package Tool;

import java.util.HashMap;
import java.util.List;

/**
 * This class contains all the information to make a Gueser find a city in a game against the computer.
 * The clues are a list of prerecorded clues from human vs human games.
 * The different methods of this class allow to manage the clues and the solution related to one city guess.
 * @author kemoadrian
 *
 */
public class Game {
	
	private Integer ID;
	private String solution;
	private Integer max_clue;
	private Integer current_clue;
	private HashMap<Integer, String> clues;
	
	/** 
	 * Creates a new instance of {@link Game}.
	 * @param id the {@link Integer} with the unique identifier of the game
	 * @param solution the {@link String} with the name of the city to guess
	 */
	public Game(Integer id, String solution){
		this.ID = id;
		this.solution = solution;
		this.max_clue = 0;
		this.current_clue = 0;
		this.clues = new HashMap<Integer,String>();
	}
	
	/**
	 * Adds a clue to the {@link List} of clues.
	 * @param s a {@link String} with the name of the clue to add
	 */
	public void addClue(String s){
		clues.put(max_clue, s);
		max_clue ++;
	}
	
	/**
	 * Get the ID of the game.
	 * @return the {@link Integer} id of the game
	 */
	public Integer getID(){
		return this.ID;
	}
	
	/**
	 * Get the solution of the game.
	 * @return the {@link String} with the name of the solution of the game
	 */
	public String getSolution(){
		return this.solution;
	}
	
	/**
	 * Get the next clue from the list.
	 * @return the {@link String} with the name of the next clue
	 */
	public String getNextClue(){
		String o = clues.get(current_clue);
		current_clue ++;
		return o;
	}
	
	/**
	 * Check if there is still a clue in the list.
	 * @return <code>true</code> if there is still a clue in the list, <code>false</code> otherwise
	 */
	public boolean stillHasClue(){
		return(!(current_clue>=max_clue));
	}
	
	public String toString(){
		return "ID : "+this.ID+" | Solution : "+this.solution+" | Clues : "+this.clues.values().toString();
	}
	
	/**
	 * Get the number of remaining clues in the list.
	 * @return the {@link Integer} number of remaining clues
	 */
	public Integer clues_remaining(){
		return max_clue - current_clue;
	}

}
