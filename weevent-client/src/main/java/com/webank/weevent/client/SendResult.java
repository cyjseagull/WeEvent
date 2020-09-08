package com.webank.weevent.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Publish function result.
 * <p>
 *
 * @author matthewliu
 * @since 2018/11/02
 */
@Getter
@Setter
@ToString
public class SendResult {
    public enum SendResultStatus {
        SUCCESS,
        TIMEOUT,
        ERROR,
        NO_PERMISSION,
    }


    /**
     * Result status.
     */
    private SendResultStatus status;

    /**
     * Topic name.
     */
    private String topic;

    /**
     * Event ID.
     */
    private String eventId;

    /**
     * Default construction needed by jackson marshall.
     */
    public SendResult() {
    }

    public SendResult(SendResultStatus status) {
        this.status = status;
    }

    public SendResultStatus getStatus() {
        return status;
    }

    public void setStatus(SendResultStatus status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
