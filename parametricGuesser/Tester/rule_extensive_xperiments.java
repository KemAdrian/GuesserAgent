package Tester;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Guesser.TopXGuesserSsoCountries;
import Tool.MemoryDistance;

/**
 * This class allows the experimenter to run numerous experiments at the same time.
 * Two {@link List} of {@link String} are defined.
 * best_countries lists the numbers of closest countries to the clue from which the cities will be tested.
 * best_cities lists the numbers of most referenced countries from the initial set that will be tested.
 * The tes is done with the Guesser {@link TopXGuesserSsoCountries}.
 * @author kemoadrian
 *
 */
public class rule_extensive_xperiments {
	
	/**
	 * Run the set of experiments.
	 * @param args no argument needed
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		MemoryDistance.DISTANCE = MemoryDistance.PMIDISTANCE;
		List<String> best_countries = Arrays.asList("1", "2", "3", "4", "5", "10", "15", "20", "25", "50", "100", "1000");
		List<String> famest_countries = Arrays.asList("10", "20", "30", "40", "50", "60", "100");
		
		for(String X1 : best_countries){
			for(String X2 : famest_countries){
				String[] param = {X1,X2};
				extensive_game_file.main(param);
			}
		}
	}

}
