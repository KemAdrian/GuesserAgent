package Tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Change the format of an experiment's output, to a format compatible with https://plot.ly/, used in the publication.
 * @author kemoadrian
 */
public class PlotFormat {
	
	/**
	 * Execute the main to change the format of the results.
	 * {@link String} input is the result that need to be transformed
	 * {@link String} ouput is the path of the desired output file
	 * @param args no args taken, modify files' paths directly in the main
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String input = "PlotResults/WordNet.csv";
		String output = "Results/FILE";

		Path file = Paths.get(output);
		List<String> lines = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(input));
		String string;
		while ((string = br.readLine()) != null) {
			String[] partitions = string.split("\\s+");

			String s = partitions[0];
			String[] tokens = s.split("B");
			String best = "";
			switch(tokens[1]){
			case "1":
				best = "0";
				break;
			case "2":
				best = "1";
				break;
			case "3":
				best = "2";
				break;
			case "4":
				best = "3";
				break;
			case "5":
				best = "4";
				break;
			case "10":
				best = "5";
				break;
			case "15":
				best = "6";
				break;
			case "20":
				best = "7";
				break;
			case "25":
				best = "8";
				break;
			case "50":
				best = "9";
				break;
			case "100":
				best = "10";
				break;
			case "ALL":
				best = "11";
				break;
			}
			String fame = "";
			if (tokens[0].length() > 0) {
				tokens = tokens[0].split("F");
				fame = "";
				switch (tokens[1]) {
				case "10":
					fame = "0";
					break;
				case "20":
					fame = "1";
					break;
				case "30":
					fame = "2";
					break;
				case "40":
					fame = "3";
					break;
				case "50":
					fame = "4";
					break;
				case "60":
					fame = "5";
					break;
				}
			} else
				fame = "6";
			System.out.println("best : " + best);
			System.out.println("famest : " + fame);

			lines.add(fame + " , " + best + " , " + partitions[3]);

		}
		Files.write(file, lines, Charset.forName("UTF-8"));
		br.close();
	}

}
