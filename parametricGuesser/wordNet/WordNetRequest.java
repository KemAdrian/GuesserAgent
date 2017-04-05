package wordNet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/**
 * This class manages the information about semantics using Wordnet.
 * @author kemoadrian
 *
 */
public class WordNetRequest {
	
	IDictionary dico;
	
	/**
	 * Open the dictionnary.
	 */
	public WordNetRequest(){
		String path = "parametricGuesser/dict";
		URL url;
		try {
			url = new URL("file", null, path);

			// construct the dictionary object and open it
			dico = new Dictionary(url);
			dico.open();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Give the semantic distance between two words using Wordnet (Jiang Conrath).
	 * @param s1 the {@link String} with the name of the first word
	 * @param s2 the {@link String} with the name of the second word
	 * @return a {@link Double} figuring the semantinc distance between the two words
	 */
	public Double distance(String s1, String s2){
		boolean exit = false;
		
		// Initialize path lists & distances
		int imax1 = 1;
		int imax2 = 1;
		
		TreeMap<Integer, List<ISynsetID>> stages_s1 = new TreeMap<>();
		TreeMap<Integer, List<ISynsetID>> stages_s2 = new TreeMap<>();
		stages_s1.put(0, new ArrayList<>());
		stages_s2.put(0, new ArrayList<>());
		
		// Start with initial terms
		for(POS pos : POS.values()){
			IIndexWord idxWord = dico.getIndexWord(s1,pos);
			if (idxWord != null) {
				for (IWordID wordID : idxWord.getWordIDs()) {
					stages_s1.get(0).add(dico.getWord(wordID).getSynset().getID());
				}
			}
		}
		
		for(POS pos : POS.values()){
			IIndexWord idxWord = dico.getIndexWord(s2,pos);
			if(idxWord != null){
				for (IWordID wordID : idxWord.getWordIDs()) {
					stages_s2.get(0).add(dico.getWord(wordID).getSynset().getID());
				}
			}
		}
		
		while(!exit){
			for(Integer i1 : stages_s1.keySet()){
				for(Integer i2 : stages_s2.keySet()){
					ISynsetID sol = hasCommonSynset(stages_s1.get(i1), stages_s2.get(i2));
					if(sol != null){
						return (2 * Math.log(getProbability(sol)+1)) - (Math.log(getProbability(stages_s1.get(0).get(0))+1) + Math.log(getProbability(stages_s2.get(0).get(0))+1));
					}
				}
			}
			List<ISynsetID> hyp1 = getHypernyms(stages_s1.get(imax1 - 1));
			List<ISynsetID> hyp2 = getHypernyms(stages_s2.get(imax2 - 1));
			if(!hyp1.isEmpty()){
				stages_s1.put(imax1,hyp1);
				imax1 ++;
			}
			if(!hyp2.isEmpty()){
				stages_s2.put(imax2,getHypernyms(stages_s2.get(imax2 - 1)));
				imax2 ++;
			}
			if(hyp1.isEmpty() && hyp2.isEmpty())
				exit = true;
		}
		return 100.;
	}
	
	/**
	 * Test if two {@link List} of {@link ISynsetID} have a {@link ISynsetID} in common.
	 * @param l1 the first {@link List} of {@link ISynsetID}
	 * @param l2 ths second {@link List} of {@link ISynsetID}
	 * @return the common {@link ISynsetID} if they have one, <code>null</code> otherwise
	 */
	private ISynsetID hasCommonSynset(List<ISynsetID> l1, List<ISynsetID> l2){
		for(ISynsetID i : l1){
			if(l2.contains(i))
				return i;
		}
		return null;
	}
	
	/**
	 * Get the probability of a {@link ISynsetID} (see publication).
	 * @param s the {@link ISynsetID} to check
	 * @return a {@link Double} figuring the probability of the {@link ISynsetID}
	 */
	public double getProbability(ISynsetID s){
		int imax = 1;
		boolean exit = false;
		
		TreeMap<Integer, List<ISynsetID>> stages = new TreeMap<>();
		List<ISynsetID> to_add = new ArrayList<>();
		to_add.add(s);
		stages.put(0, to_add);
		while(!exit){
			List<ISynsetID> new_l = new ArrayList<>(); 
			for(ISynsetID i : stages.get(imax - 1)){
				new_l.addAll(dico.getSynset(i).getRelatedSynsets(Pointer.HYPONYM));
				new_l.addAll(dico.getSynset(i).getRelatedSynsets(Pointer.HYPONYM_INSTANCE));
			}
			if(new_l.isEmpty())
				exit = true;
			else{
				stages.put(imax, new_l);
				imax ++;
			}
		}
		double out = 0.;
		for(Integer i : stages.keySet()){
			out += stages.get(i).size();
		}
		return out/17659;
	}
	
	/**
	 * Give all the hypernyms of a {@link List} of {@link ISynsetID}.
	 * @param l the {@link List} of {@link ISynsetID}
	 * @return the {@link List} of hypernyms
	 */
	private List<ISynsetID> getHypernyms(List<ISynsetID> l){
		List<ISynsetID> output = new LinkedList<>();
		for(ISynsetID i : l){
			output.addAll(dico.getSynset(i).getRelatedSynsets(Pointer.HYPERNYM ));
			output.addAll(dico.getSynset(i).getRelatedSynsets(Pointer.HYPERNYM_INSTANCE ));
		}
		return output;
	}
	
	/**
	 * Give a {@link String} displaying all the {@link IWord} associated to each {@link ISynsetID} from a {@link List}.
	 * @param s the {@link List} of {@link ISynsetID}
	 * @return the {@link String} with the associated words
	 */
	public String display(List<ISynsetID> s){
		String out = "";
		for(ISynsetID i : s){
			for(IWord w : dico.getSynset(i).getWords()){
				out += w.getLemma() + " ";
			}
		}
		return out;
	}

}
