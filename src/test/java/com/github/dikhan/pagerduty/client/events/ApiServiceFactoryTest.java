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
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, false);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
    }

    @Test
    public void apiServiceFactoryWithProxyParamsProducesRightDefaultApiServiceImplWithRetries() {
        String eventApi = "eventApi";
        String proxyHost = "localhost";
        Integer proxyPort = 8080;
        boolean doRetries = true;
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, proxyHost, proxyPort, doRetries);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, proxyHost, proxyPort, doRetries);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
        // reset unirest settings
        Unirest.setProxy(null);
    }

    @Test
    public void apiServiceFactoryWithProxyParamsProducesRightDefaultApiServiceImplWithoutRetries() {
        String eventApi = "eventApi";
        String proxyHost = "localhost";
        Integer proxyPort = 8080;
        boolean doRetries = false;
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, proxyHost, proxyPort, false);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, proxyHost, proxyPort, false);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
        // reset unirest settings
        Unirest.setProxy(null);
    }
}