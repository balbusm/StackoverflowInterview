package com.polidea.stackoverflowinterview;

public interface ModifiableInjector extends Injector {
    /**
     * Sets new modules. Method useful during tests
     * @param modules
     */
    public void modifyInjector(Object... modules);
}
