package com.github.dikhan.utils;

import com.github.dikhan.domain.EventResult;
import org.apache.http.HttpStatus;

public class EventHelper {

    /**
     * Helper method to create a successful event result
     * @param dedupKey this would be the dedup key auto-generated by PagerDuty
     * @return successful event result
     */
    public static EventResult successEvent(String dedupKey) {
        return EventResult.successEvent("success", "Event processed", dedupKey);
    }

    /**
     * Helper method to create an error event result
     * @return error event result
     */
    public static EventResult errorEvent() {
        return EventResult.errorEvent("invalid event", "Event object is invalid", "[\"some error from upstream server...\"]");
    }

    /**
     * Helper method to create an unexpected event result, by default containing a forbidden status code
     * @return error event result
     */
    public static EventResult unexpectedErrorEvent() {
        return EventResult.errorEvent(String.valueOf(HttpStatus.SC_FORBIDDEN), "", "");
    }
}