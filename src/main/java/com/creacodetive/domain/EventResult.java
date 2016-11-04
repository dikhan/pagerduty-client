package com.creacodetive.domain;

public class EventResult {

    private final String status;
    private final String message;
    private final String incidentKey;

    public EventResult(String status, String message, String incidentKey) {
        this.status = status;
        this.message = message;
        this.incidentKey = incidentKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventResult that = (EventResult) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return !(incidentKey != null ? !incidentKey.equals(that.incidentKey) : that.incidentKey != null);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (incidentKey != null ? incidentKey.hashCode() : 0);
        return result;
    }
}
