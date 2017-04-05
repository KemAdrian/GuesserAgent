package wikipedia;

/**
 * Manage the queries to Wikipedia related to the calculus of semantic distances
 * @author kemoadrian
 *
 */
public class WikiResult {

	private Query query;

	/**
	 * Get the total of hits of a query in Wikipedia.
	 * @return the {@link Integer} number of hits
	 */
	public Integer totalHit() {
		if (query == null)
			return null;
		return query.getInfo();
	}

	/**
	 * Store the result of a query to Wikipedia in an object {@link Query}.
	 * @author kemoadrian
	 *
	 */
	static class Query {
		public SearchInfo searchinfo;

		Integer getInfo() {
			return searchinfo.totalhits;
		}
	}

	/**
	 * Look for the total hit of a query in a {@link Query} object.
	 * @author kemoadrian
	 *
	 */
	static class SearchInfo {
		public Integer totalhits;
	}

}
