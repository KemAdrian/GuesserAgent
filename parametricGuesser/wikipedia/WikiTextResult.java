package wikipedia;

/**
 * Manage the queries to Wikipedia related to their textual information.
 * @author kemoadrian
 *
 */
public class WikiTextResult {
	
	public static final String test = "22989";

	private Query query;

	/**
	 * Get the text of a queried page.
	 * @return a {@link String} with the text from the queried page
	 */
	public String text() {
		return query.pages.page.extract;
	}

	/**
	 * Store the result of a query to Wikipedia in an object {@link Query}.
	 * @author kemoadrian
	 *
	 */
	static class Query {
		public static String[] pageids; 
		public Pages pages;
	}
	
	/**
	 * Store the a list of Wikipedia pages in an object {@link Pages}.
	 * @author kemoadrian
	 *
	 */
	static class Pages {
		Page page;
	}
	
	/**
	 * Store a Wikipedia page in an object {@link Page}.
	 * @author kemoadrian
	 *
	 */
	static class Page {
		String extract;
	}

}
