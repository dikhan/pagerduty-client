package com.github.dikhan.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import junit.framework.AssertionFailedError;
import org.junit.Test;
import sun.jvm.hotspot.utilities.AssertionFailure;

import java.io.IOException;

public class PayloadBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreatePayloadIfMandatoryFieldSummaryIsMissing() {
        Payload.Builder.newBuilder().setSeverity(Severity.INFO).setSource("source").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreatePayloadIfMandatoryFieldSourceIsMissing() {
        Payload.Builder.newBuilder().setSeverity(Severity.INFO).setSummary("summary").build();
    }

    @Test(expected = NullPointerException.class)
    public void notAbleToCreatePayloadIfMandatoryFieldSeverityIsMissing() {
        Payload.Builder.newBuilder().setSummary("summary").setSource("source").build();
    }

    @Test
    public void successfulCreation() {
        Payload.Builder.newBuilder().setSummary("summary").setSource("source")
                .setSeverity(Severity.INFO).setCustomDetails("details").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomeDetailInvalidJsonFormat() {
        String details = "{\"Name\":\"hello\",}";

        Payload.Builder.newBuilder().setSummary("summary").setSource("source")
                .setSeverity(Severity.INFO).setCustomDetails(details).build();
    }

    @Test
    public void testCustomeDetailWithValidJson() {
        String details = "{\"Name\":\"Hello\"}";

        Payload.Builder.newBuilder().setSummary("summary").setSource("source")
                .setSeverity(Severity.INFO).setCustomDetails(details).build();
    }
}
