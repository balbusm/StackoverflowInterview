package com.polidea.stackoverflowinterview;

import android.app.Application;

import dagger.ObjectGraph;

public class StackOverflowApplication extends Application implements ModifiableInjector {

    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(getModules());
    }

    public Object[] getModules() {
        return new Object[]{
                new ClientModule()
        };
    }

    @Override
    public void modifyInjector(Object... modules) {
        graph = ObjectGraph.create(modules);
    }

    @Override
    public Object inject(Object object) {
        return graph.inject(object);
    }
}
