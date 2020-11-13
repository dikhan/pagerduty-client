package com.github.dikhan.pagerduty.client.events;

import com.mashape.unirest.http.Unirest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiServiceFactoryTest {

    @Test
    public void apiServiceFactoryProducesRightDefaultApiServiceImpl() {
        String eventApi = "eventApi";
        String changeEventApi = "changeEventApi";
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, changeEventApi);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, changeEventApi, false);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
    }

    @Test
    public void apiServiceFactoryWithProxyParamsProducesRightDefaultApiServiceImplWithRetries() {
        String eventApi = "eventApi";
        String changeEventApi = "changeEventApi";
        String proxyHost = "localhost";
        Integer proxyPort = 8080;
        boolean doRetries = true;
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, changeEventApi, proxyHost, proxyPort, doRetries);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, changeEventApi, proxyHost, proxyPort, doRetries);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
        // reset unirest settings
        Unirest.setProxy(null);
    }

    @Test
    public void apiServiceFactoryWithProxyParamsProducesRightDefaultApiServiceImplWithoutRetries() {
        String eventApi = "eventApi";
        String changeEventApi = "changeEventApi";
        String proxyHost = "localhost";
        Integer proxyPort = 8080;
        boolean doRetries = false;
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, changeEventApi, proxyHost, proxyPort, false);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, changeEventApi, proxyHost, proxyPort, false);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
        // reset unirest settings
        Unirest.setProxy(null);
    }
}