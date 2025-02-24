package org.machinecode.inmemorycache;
/*
OPTIONAL CLASS CAN USE THIS OR Strategy

CacheFactory

Design Pattern: Factory Pattern
The CacheFactory class encapsulates the logic for creating a cache instance based on a given type (e.g., "LRU" or "LFU"). This decouples object creation from object usage, meaning that the client (in LruCacheMt.main()) doesn't need to know about the concrete classes—it simply asks the factory for an ICache instance.

OOP Principle: Loose Coupling
By using a factory, the code adheres to the Dependency Inversion Principle. The client code depends on an abstraction (ICache) rather than a concrete implementation, making it easier to extend or modify caching strategies in the future.


 */
public class CacheFactory {
    public static CacheT createCache(String type, int capacity) {
        if (type.equals("LRU")) {
            return new LruCache(capacity);
        } else if (type.equals("LFU")) {
            return null;
        }
        return new LruCache(capacity);
    };
}
