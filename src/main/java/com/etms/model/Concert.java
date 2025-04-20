package com.etms.model;

public class Concert extends Event {
    private String artistName;
    private String genre;

    public Concert() {
        super();
    }

    public Concert(int eventId, String eventName, String description, String artistName, String genre) {
        super(eventId, eventName, description, null, null, null, EventType.Concert, 0);
        this.artistName = artistName;
        this.genre = genre;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "eventId=" + getEventId() +
                ", eventName='" + getEventName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", artistName='" + artistName + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}