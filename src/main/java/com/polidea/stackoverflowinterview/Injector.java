package com.polidea.stackoverflowinterview;

public interface Injector {
    /**
     * Injects dependencies into given object
     * @param object
     * @return
     */
    public Object inject(Object object);
}
