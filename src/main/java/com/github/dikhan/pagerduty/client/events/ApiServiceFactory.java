package com.github.dikhan.pagerduty.client.events;

import org.apache.commons.lang3.StringUtils;

/**
 * This class is in charge of producing implementations of ApiService. The default impl can be retrieved
 * by calling {@link ApiServiceFactory#getDefault()} method. In the case where a new impl is preferred, that
 * would be the method to update making the factory return the new implementation configured instead.
 */
public class ApiServiceFactory {

    private final String eventApi;
    private final String changeEventApi;
    private final String proxyHost;
    private final Integer proxyPort;
    private final Boolean doRetries;

    public ApiServiceFactory(String eventApi, String changeEventApi) {
        this(eventApi, changeEventApi, null, null, false);
    }

    public ApiServiceFactory(String eventApi, String changeEventApi, String proxyHost, Integer proxyPort) {
        this(eventApi, changeEventApi, proxyHost, proxyPort, false);
    }

    public ApiServiceFactory(String eventApi, String changeEventApi, String proxyHost, Integer proxyPort, Boolean doRetries) {
        this.eventApi = eventApi;
        this.changeEventApi = changeEventApi;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.doRetries = doRetries;
    }

    public ApiService getDefault() {
        if (!StringUtils.isEmpty(proxyHost) && proxyPort!= null) {
            return new HttpApiServiceImpl(eventApi, changeEventApi, proxyHost, proxyPort, doRetries);
        }
        return new HttpApiServiceImpl(eventApi, changeEventApi, doRetries);
    }

}
