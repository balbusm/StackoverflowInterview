package com.polidea.stackoverflowinterview.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Summary implements Parcelable{

    private final Owner owner;
    private final String title;
    private final int answers;
    private final String link;

    public Summary(Owner owner, String title, int answers, String link) {
        this.owner = owner;
        this.title = title;
        this.answers = answers;
        this.link = link;
    }

    public int getAnswersCount() {
        return answers;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    // Parcelling part
    public Summary(Parcel in){
        owner = (Owner) in.readValue(null);
        title = in.readString();
        answers = in.readInt();
        link = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(owner);
        out.writeString(title);
        out.writeInt(answers);
        out.writeString(link);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Summary createFromParcel(Parcel in) {
            return new Summary(in);
        }

        public Summary[] newArray(int size) {
            return new Summary[size];
        }
    };

    @Override
    public String toString() {
        return String.format("[owner=%s title=%s answers=%s link=%s]", owner, title, answers, link);
    }
}