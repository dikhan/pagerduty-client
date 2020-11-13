package com.github.dikhan.pagerduty.client.events.utils;

import com.github.dikhan.pagerduty.client.events.domain.ChangeEvent;
import com.github.dikhan.pagerduty.client.events.domain.ChangeEventPayload;

public class ChangeEventHelper {

    public static ChangeEvent prepareSampleChangeEvent(String routingKey) {
        return ChangeEvent.ChangeEventBuilder
                .newBuilder(routingKey, ChangeEventPayload.Builder.newBuilder()
                        .setSummary("testing summary")
                        .setSource("testing source")
                        .build())
                .build();
    }
}
