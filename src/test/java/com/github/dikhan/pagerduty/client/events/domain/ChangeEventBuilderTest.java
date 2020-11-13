package com.github.dikhan.pagerduty.client.events.domain;

import com.github.dikhan.pagerduty.client.events.exceptions.NotifyEventException;
import org.junit.Test;

public class ChangeEventBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void notAbleToCreateChangeIfMandatoryFieldRoutingKeyIsNull() throws NotifyEventException {
        ChangeEventPayload payload = ChangeEventPayload.Builder.newBuilder().build();
        ChangeEvent.ChangeEventBuilder.newBuilder(null, payload);
    }
}
