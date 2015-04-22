package com.polidea.stackoverflowinterview.domain;

import android.graphics.Bitmap;

public class Owner {

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

}
