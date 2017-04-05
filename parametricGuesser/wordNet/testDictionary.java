package wordNet;

import java.io.IOException;

import Tool.MemoryDistance;

/**
 * Sandbox to test the WordNet Library used in the code.
 * @author kemoadrian
 *
 */
public class testDictionary {
	
	/**
	 * Put your code here if you want to test how the wordnet requests are made.
	 * @param args depends
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		/*String path = "parametricGuesser/dict";
		URL url = new URL ("file", null , path ) ;
		
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary ( url ) ;
		dict . open () ;
		
		// look up first sense of the word "dog "
		IIndexWord idxWord = dict . getIndexWord ("italy", POS . NOUN ) ;
		IWordID wordID = idxWord . getWordIDs () . get (0) ;
		IWord word = dict . getWord ( wordID ) ;
		System . out . println ("Id = " + wordID ) ;
		System . out . println (" Lemma = " + word . getLemma () ) ;
		System . out . println (" Gloss = " + word . getSynset () . getGloss () ) ;
		
		// get the synset
		ISynset synset = word . getSynset () ;
		
		// get the hypernyms
		List < ISynsetID > hypernyms =
		synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
		
		// print out each h y p e r n y m s id and synonyms
		List < IWord > words ;
		for( ISynsetID sid : hypernyms ) {
		words = dict . getSynset ( sid ) . getWords () ;
		System . out . print ( sid + " {") ;
		for( Iterator < IWord > i = words . iterator () ; i . hasNext () ;) {
		System . out . print ( i . next () . getLemma () ) ;
		if( i . hasNext () )
		System . out . print (", ") ;
		}
		System . out . println ("}") ;
		}
		
		WorldNetRequest wnr = new WorldNetRequest();
		System.out.println(wnr.distance("inca", "peru"));*/
		
		//System.out.println(WikipediaRequest.getHits(Arrays.asList("the")));
		//System.out.println(WikipediaRequest.getHits(Arrays.asList("pichu")));
		//System.out.println(WikipediaRequest.getHits(Arrays.asList("machu","pichu")));
		System.out.println(MemoryDistance.distance("communism", "dog"));
	}

}
