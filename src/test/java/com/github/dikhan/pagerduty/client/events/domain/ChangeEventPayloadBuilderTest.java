package com.github.dikhan.pagerduty.client.events.domain;

import org.json.JSONObject;
import org.junit.Test;

public class ChangeEventPayloadBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreatePayloadIfMandatoryFieldSummaryIsMissing() {
        ChangeEventPayload.Builder.newBuilder().build();
    }

    @Test
    public void successfulCreation() {
        ChangeEventPayload.Builder.newBuilder().setSummary("summary").build();
    }

    @Test
    public void testCustomDetailWithValidJson() {
        JSONObject details = new JSONObject("{\"Name\":\"Hello\"}");

        ChangeEventPayload.Builder.newBuilder().setSummary("summary")
                .setCustomDetails(details).build();
    }
}
