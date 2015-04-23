package com.polidea.stackoverflowinterview.search;

import com.polidea.stackoverflowinterview.domain.Summary;

import java.util.List;

public interface JsonParser {
    /**
     * Parses json to List<Summary>, throws ClientException when unable to parse
     * @param json
     * @return
     * @throws ClientException
     */
    public List<Summary> parseToListSummary(String json) throws ClientException;
}
