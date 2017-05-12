package com.github.dikhan.domain;

public class EventResult {

    private final String status;
    private final String message;
    private final String dedupKey;
    private final String errors;

    private EventResult(EventResultBuilder eventResultBuilder) {
        this.status = eventResultBuilder.getStatus();
        this.message = eventResultBuilder.getMessage();
        this.dedupKey = eventResultBuilder.getDedupKey();
        this.errors = eventResultBuilder.getErrors();
    }

    public static EventResult successEvent(String status, String message, String dedupKey) {
        return new EventResultBuilder(status, message).success(dedupKey).build();
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

    public String getDedupKey() {
        return dedupKey;
    }

    public String getErrors() {
        return errors;
    }

    private static class EventResultBuilder {
        private final String status;
        private final String message;
        private String dedupKey;
        private String errors;

        public EventResultBuilder(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public EventResultBuilder success(String dedupKey) {
            this.dedupKey = dedupKey;
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

        public String getDedupKey() {
            return dedupKey;
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
        if (dedupKey != null ? !dedupKey.equals(that.dedupKey) : that.dedupKey != null) return false;
        return !(errors != null ? !errors.equals(that.errors) : that.errors != null);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (dedupKey != null ? dedupKey.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventResult{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", dedupKey='" + dedupKey + '\'' +
                ", errors='" + errors + '\'' +
                '}';
    }
}
