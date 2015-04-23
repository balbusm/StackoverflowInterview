package com.polidea.stackoverflowinterview.search;


import android.os.Parcel;
import android.os.Parcelable;

import com.polidea.stackoverflowinterview.domain.Summary;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps results of the http request
 */
public class QueryResult implements Parcelable {
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

    // Parcelling part
    public QueryResult(Parcel in) {
        summaryList = new ArrayList<Summary>();
        in.readList(summaryList, null);
        hasError = fromByte(in.readByte());
        errorCode = fromInt(in.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(summaryList);
        out.writeByte(toByte(hasError));
        out.writeInt(toInt(errorCode));
    }

    private ErrorCode fromInt(int i) {
        if (i < 0) {
            return null;
        } else {
            return ErrorCode.values()[i];
        }
    }


    private int toInt(ErrorCode code) {
        if (code == null) {
            return -1;
        } else {
            return code.ordinal();
        }
    }

    private byte toByte(boolean b) {
        return b ? (byte) 1 : (byte) 0;
    }

    private boolean fromByte(byte b) {
        return b != 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public QueryResult createFromParcel(Parcel in) {
            return new QueryResult(in);
        }

        public QueryResult[] newArray(int size) {
            return new QueryResult[size];
        }
    };


    public enum ErrorCode {
        OK,
        NO_RESULT,
        SERVER_ERROR,
        CONNECTION_ERROR,
        DATA_ERROR,
        UNSPECIFIED_ERROR
    }
}
