package com.e.photounsplasht.data.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private final String clientId;

    public HeaderInterceptor(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        return chain.proceed(
                chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID " + clientId)
                        .build()
        );
    }
}