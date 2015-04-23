package com.polidea.stackoverflowinterview.search;

public interface StackOverflowClient {
    /**
     * Retrieves data from the server by using arguments
     *
     * Note that QueryResult can contain Error Code instead of proper results
     * @param queryArgument
     * @return
     */
    public QueryResult getResults(QueryArgument queryArgument);
}
