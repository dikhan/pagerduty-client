package com.creacodetive;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiServiceFactoryTest {

    @Test
    public void apiServiceFactoryProducesRightDefaultApiServiceImpl() {
        String eventApi = "eventApi";
        String accessKey = "accessKey";
        ApiServiceFactory apiServiceFactory = new ApiServiceFactory(eventApi, accessKey);

        ApiService apiService = apiServiceFactory.getDefault();
        HttpApiServiceImpl httpApiService = new HttpApiServiceImpl(eventApi, accessKey);
        assertThat(apiService).isExactlyInstanceOf(HttpApiServiceImpl.class);
        assertThat(apiService).isEqualTo(httpApiService);
    }
}