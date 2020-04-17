package com.github.dikhan.pagerduty.client.events;

import com.mashape.unirest.http.Unirest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiServiceFactoryTest {

    @Test
    public void apiServiceFactoryProducesRightDefaultApiServiceImpl() {
        String eventApi = "eventApi";
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
    }

    @Test
    public void apiServiceFactoryWithProxyParamsProducesRightDefaultApiServiceImpl() {
        String eventApi = "eventApi";
        String proxyHost = "localhost";
        Integer proxyPort = 8080;
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, proxyHost, proxyPort);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, proxyHost, proxyPort);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
        // reset uni rest settings
        Unirest.setProxy(null);
    }
}