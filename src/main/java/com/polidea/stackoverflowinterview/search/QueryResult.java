package com.polidea.stackoverflowinterview.search;


import com.polidea.stackoverflowinterview.domain.Summary;

import java.util.List;

public class QueryResult {
    private final List<Summary> summaryList;
    private final boolean hasError;
    private final ErrorCode errorCode;

    public QueryResult(List<Summary> summaryList) {
        this(summaryList, null);
    }

    public QueryResult(ErrorCode errorCode) {
        this(null, errorCode);
    }

    private QueryResult(List<Summary> summaryList, ErrorCode errorCode) {
        this.summaryList = summaryList;
        this.errorCode = errorCode;
        this.hasError = errorCode != null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public boolean hasError() {
        return hasError;
    }

    public List<Summary> getSummaryList() {
        return summaryList;
    }

    public enum ErrorCode {
        OK,
        NO_RESULT,
        SERVER_ERROR,
        CONNECTION_ERROR,
        DATA_ERROR,
        UNSPECIFIED_ERROR
    }
}
