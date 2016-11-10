package com.github.dikhan.domain;

public class EventResult {

    private final String status;
    private final String message;
    private final String incidentKey;
    private final String errors;

    private EventResult(EventResultBuilder eventResultBuilder) {
        this.status = eventResultBuilder.getStatus();
        this.message = eventResultBuilder.getMessage();
        this.incidentKey = eventResultBuilder.getIncidentKey();
        this.errors = eventResultBuilder.getErrors();
    }

    public static EventResult successEvent(String status, String message, String incidentKey) {
        return new EventResultBuilder(status, message).success(incidentKey).build();
    }

    public static EventResult errorEvent(String status, String message, String errors) {
        return new EventResultBuilder(status, message).error(errors).build();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public String getErrors() {
        return errors;
    }

    private static class EventResultBuilder {
        private final String status;
        private final String message;
        private String incidentKey;
        private String errors;

        public EventResultBuilder(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public EventResultBuilder success(String incidentKey) {
            this.incidentKey = incidentKey;
            return this;
        }

        public EventResultBuilder error(String errors) {
            this.errors = errors;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getIncidentKey() {
            return incidentKey;
        }

        public String getErrors() {
            return errors;
        }

        public EventResult build() {
            return new EventResult(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventResult that = (EventResult) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (incidentKey != null ? !incidentKey.equals(that.incidentKey) : that.incidentKey != null) return false;
        return !(errors != null ? !errors.equals(that.errors) : that.errors != null);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (incidentKey != null ? incidentKey.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventResult{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", incidentKey='" + incidentKey + '\'' +
                ", errors='" + errors + '\'' +
                '}';
    }
}
