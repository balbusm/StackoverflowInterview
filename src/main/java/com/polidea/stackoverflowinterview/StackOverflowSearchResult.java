package com.polidea.stackoverflowinterview;

import android.graphics.Bitmap;

import java.net.URL;

public class StackOverflowSearchResult {
    String title;
    URL link;
    int answers;
    Bitmap authorAvatar;
    String authorName;

    public StackOverflowSearchResult(String title, URL link, int answers, Bitmap authorAvatar, String authorName) {

        this.title = title;
        this.link = link;
        this.answers = answers;
        this.authorAvatar = authorAvatar;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public URL getLink() {
        return link;
    }

    public int getAnswers() {
        return answers;
    }

    public Bitmap getAuthorAvatar() {
        return authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }
}
