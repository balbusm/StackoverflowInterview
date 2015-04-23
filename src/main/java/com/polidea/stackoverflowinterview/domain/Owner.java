package com.polidea.stackoverflowinterview.domain;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Owner implements Parcelable {

    private final String displayName;
    private final Bitmap profileImage;

    public Owner(String displayName, Bitmap profileImage) {
        this.displayName = displayName;
        this.profileImage = profileImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    // Parcelling part
    public Owner(Parcel in){
       displayName = in.readString();
       profileImage = (Bitmap)in.readValue(null);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(displayName);
        out.writeValue(profileImage);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    @Override
    public String toString() {
        return String.format("[displayName=%s hasProfileImage=%s]", displayName, profileImage != null);
    }
}

