package com.polidea.stackoverflowinterview.search;

import android.os.Parcel;
import android.os.Parcelable;

import static com.google.common.base.Preconditions.checkNotNull;

public class QueryArgument implements Parcelable {
    public static final Parcelable.Creator<QueryArgument> CREATOR
            = new Parcelable.Creator<QueryArgument>() {
        public QueryArgument createFromParcel(Parcel in) {
            return new QueryArgument(in);
        }

        public QueryArgument[] newArray(int size) {
            return new QueryArgument[size];
        }
    };
    private final String intitle;
    private final Order order;
    private final Sort sort;

    public QueryArgument(String intitle, Order order, Sort sort) {
        this.intitle = checkNotNull(intitle);
        this.order = checkNotNull(order);
        this.sort = checkNotNull(sort);
    }

    public QueryArgument(Parcel in) {
        this.intitle = in.readString();
        this.order = Order.values()[in.readInt()];
        this.sort = Sort.values()[in.readInt()];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(intitle);
        parcel.writeInt(order.ordinal());
        parcel.writeInt(sort.ordinal());
    }

    public Order getOrder() {
        return order;
    }

    public Sort getSort() {
        return sort;
    }

    public String getIntitle() {
        return intitle;
    }

    public enum Order {
        ASC,
        DESC
    }

    public enum Sort {
        ACTIVITY,
        VOTES,
        CREATION,
        RELEVANCE
    }
}
