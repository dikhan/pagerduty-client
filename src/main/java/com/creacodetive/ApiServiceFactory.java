package com.creacodetive;

/**
 * This class is in charge of producing implementations of ApiService. The default impl can be retrieved
 * by calling {@link ApiServiceFactory#getDefault()} method. In the case where a new impl is preferred, that
 * would be the method to update making the factory return the new implementation configured instead.
 */
public class ApiServiceFactory {

    private final String eventApi;

    public ApiServiceFactory(String eventApi) {
        this.eventApi = eventApi;
    }

    public ApiService getDefault() {
        return new HttpApiServiceImpl(eventApi);
    }

}
