package com.polidea.stackoverflowinterview.search;

import android.util.Log;

import com.polidea.stackoverflowinterview.domain.Summary;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import static com.polidea.stackoverflowinterview.search.QueryResult.ErrorCode;

public class RestStackOverflowClient implements StackOverflowClient {
    public static final String TAG = RestStackOverflowClient.class.getSimpleName();
    public static final String BASE_STACK_EXCHANGE = "http://api.stackexchange.com/2.2/search?";
    public static final String ORDER = "order";
    public static final String SORT = "sort";
    public static final String INTITLE = "intitle";
    public static final String ARG_TEMPLATE = "&%s=%s";

    @Inject
    HttpResolver resolver;
    @Inject
    JsonParser parser;

    private String applyArgument(String name, Object value) {
        return String.format(ARG_TEMPLATE, name, value.toString());
    }

    @Override
    public QueryResult getResults(QueryArgument queryArgument) {
        final String jsonResult;
        try {
            final String preparedQuery = prepareQuery(queryArgument);
            Log.d(TAG, "Requesting " + preparedQuery);
            jsonResult = resolver.getContent(preparedQuery);
            Log.d(TAG, "Received JSON " + jsonResult);
            final List<Summary> summaries = parser.parseToListSummary(jsonResult);
            logSummaries(summaries);
            return createQueryResult(summaries);
        } catch (ClientException e) {
            Log.e(TAG, "Exception while getting result from server", e);
            return new QueryResult(e.getErrorCode());
        }
    }

    private void logSummaries(List<Summary> summaries) {
        if (!Log.isLoggable(TAG, Log.DEBUG)) {
            return;
        }
        for(Summary summary : summaries) {
            Log.d(TAG, "Result summary: " + summary);
        }
    }

    private String prepareQuery(QueryArgument queryArgument) throws ClientException {
        try {
            StringBuilder builder = new StringBuilder(BASE_STACK_EXCHANGE);
            final String encodedIntitle = URLEncoder.encode(queryArgument.getIntitle(), "UTF-8");
            builder.append(applyArgument(INTITLE, encodedIntitle));
            builder.append(applyArgument(ORDER, queryArgument.getOrder()));
            builder.append(applyArgument(SORT, queryArgument.getSort()));
            builder.append(applyArgument("site", "stackoverflow"));
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new ClientException(QueryResult.ErrorCode.UNSPECIFIED_ERROR, e);
        }
    }

    private QueryResult createQueryResult(List<Summary> summaries) {
        if (summaries.isEmpty()) {
            return new QueryResult(ErrorCode.NO_RESULT);
        }
        return new QueryResult(summaries);
    }

}