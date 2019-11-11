package com.github.dikhan.pagerduty.client.events;

import org.apache.commons.lang3.StringUtils;

/**
 * This class is in charge of producing implementations of ApiService. The default impl can be retrieved
 * by calling {@link ApiServiceFactory#getDefault()} method. In the case where a new impl is preferred, that
 * would be the method to update making the factory return the new implementation configured instead.
 */
public class ApiServiceFactory {

    private final String eventApi;
    private final String proxyHost;
    private final Integer proxyPort;

    public ApiServiceFactory(String eventApi) {
        this(eventApi, null, null);
    }

    public ApiServiceFactory(String eventApi, String proxyHost, Integer proxyPort) {
        this.eventApi = eventApi;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public ApiService getDefault() {
        if (!StringUtils.isEmpty(proxyHost) && proxyPort!= null) {
            return new HttpApiServiceImpl(eventApi, proxyHost, proxyPort);
        }
        return new HttpApiServiceImpl(eventApi);
    }

}
