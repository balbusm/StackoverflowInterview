package com.polidea.stackoverflowinterview;

public interface ModifiableInjector extends Injector {
    public void modifyInjector(Object... modules);
}
