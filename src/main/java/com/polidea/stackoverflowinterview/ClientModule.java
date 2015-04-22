package com.polidea.stackoverflowinterview;

import com.polidea.stackoverflowinterview.search.RestStackOverflowClient;
import com.polidea.stackoverflowinterview.search.StackOverflowClient;
import com.polidea.stackoverflowinterview.ui.QueryTaskFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = QueryTaskFragment.class)
public class ClientModule {

    public
    @Singleton
    @Provides
    StackOverflowClient provideStackOverflowClient() {
        return new RestStackOverflowClient();
    }
}
