package com.polidea.stackoverflowinterview;

import com.polidea.stackoverflowinterview.search.HttpResolver;
import com.polidea.stackoverflowinterview.search.HttpResolverImpl;
import com.polidea.stackoverflowinterview.search.JsonParser;
import com.polidea.stackoverflowinterview.search.JsonParserImpl;
import com.polidea.stackoverflowinterview.search.RestStackOverflowClient;
import com.polidea.stackoverflowinterview.search.StackOverflowClient;
import com.polidea.stackoverflowinterview.ui.QueryTaskFragment;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {QueryTaskFragment.class, RestStackOverflowClient.class, JsonParserImpl.class, HttpResolverImpl.class})
public class ClientModule {

    @Singleton
    @Provides
    public StackOverflowClient provideStackOverflowClient(RestStackOverflowClient client) {
        return client;
    }

    @Singleton
    @Provides
    public HttpClient provideHttpClient() {
        return new DefaultHttpClient();
    }

    @Singleton
    @Provides
    public HttpResolver provideHttpResolver(HttpResolverImpl httpResolver) {
        return httpResolver;
    }

    @Singleton
    @Provides
    public JsonParser provideJsonParser(JsonParserImpl jsonParser) {
        return jsonParser;
    }

}
