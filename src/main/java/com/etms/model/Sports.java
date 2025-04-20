package com.etms.model;

public class Sports extends Event {
    private String sportType;
    private String teams;

    public Sports() {
        super();
    }

    public Sports(int eventId, String eventName, String description, String sportType, String teams) {
        super(eventId, eventName, description, null, null, null, EventType.Sports, 0);
        this.sportType = sportType;
        this.teams = teams;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Sports{" +
                "eventId=" + getEventId() +
                ", eventName='" + getEventName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", sportType='" + sportType + '\'' +
                ", teams='" + teams + '\'' +
                '}';
    }
}