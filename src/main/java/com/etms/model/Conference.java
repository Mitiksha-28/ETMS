package com.etms.model;

public class Conference extends Event {
    private String topic;
    private String speakers;

    public Conference() {
        super();
    }

    public Conference(int eventId, String eventName, String description, String topic, String speakers) {
        super(eventId, eventName, description, null, null, null, EventType.Conference, 0);
        this.topic = topic;
        this.speakers = speakers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "eventId=" + getEventId() +
                ", eventName='" + getEventName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", topic='" + topic + '\'' +
                ", speakers='" + speakers + '\'' +
                '}';
    }
}