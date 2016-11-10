package com.github.dikhan;

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
}