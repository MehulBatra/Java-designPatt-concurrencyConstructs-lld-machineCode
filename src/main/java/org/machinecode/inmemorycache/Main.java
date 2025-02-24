package org.machinecode.inmemorycache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*

Main Class

OOP Principle: Separation of Concerns
The main class focuses on using the cache rather than on how the cache is implemented. It delegates the decision of which caching mechanism to use to the CacheFactory.

Dynamic Behavior:
The decision of which cache to use is determined at runtime (based on input arguments). This demonstrates how the code can easily adapt to different requirements without changing the core logic.

 */

public class Main {
    public static void main(final String[] args) throws InterruptedException {
        // factory method
        CacheT cache = CacheFactory.createCache("LRU", 10);
        // Strategy method
        //  LruCache lruCache = new LruCache(10);
        // CacheContextStrategy strategy = new CacheContextStrategy(lruCache);
        //change dynamically
        //cacheContext.setCacheStrategy(new LFUCacheMechanism(10));
        System.out.println("Using" + cache);
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            executorService.submit(
                    () -> {
                        cache.put(10, 1);
                        System.out.println("Put (10, 1) using " + cache.toString());

                    }
            );
        executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        cache.put(20, 2);
                                        System.out.println("Put (20, 2) using " + cache.toString());

                                    }
                                }
        );
        executorService.submit( () ->System.out.println("Get key 1: " + cache.get(10)));
            executorService.submit( () ->System.out.println("Get key 1: " + cache.get(20)));
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        }
    }
}

