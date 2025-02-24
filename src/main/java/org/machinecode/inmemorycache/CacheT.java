package org.machinecode.inmemorycache;

/*
OOP Principle: Abstraction & Polymorphism
The ICache interface defines the core cache operations (get and put). This abstraction allows different caching mechanisms to be used interchangeably, as any class that implements ICache can be substituted without changing the client code.

Design Pattern: Strategy Pattern (Partially)
By defining multiple implementations (e.g., LRUCacheMechanism and LFUCacheMechanism) that share the same interface, the code allows the caching strategy to be selected at runtime. The client only cares about the interface, not the underlying algorithm.
 */



public interface CacheT {

    int get(int key);
    void put(int key, int value);

}
