package com.polidea.stackoverflowinterview.domain;

public class Summary {

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
}
