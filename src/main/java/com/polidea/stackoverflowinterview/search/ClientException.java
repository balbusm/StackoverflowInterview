package com.polidea.stackoverflowinterview.search;

import static com.polidea.stackoverflowinterview.search.QueryResult.ErrorCode;

public class ClientException extends Exception {
    private final ErrorCode mErrorCode;

    public ClientException(ErrorCode errorCode, Exception exception) {
        super(exception);
        mErrorCode = errorCode;
    }

    public ClientException(ErrorCode errorCode) {
        mErrorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

}
