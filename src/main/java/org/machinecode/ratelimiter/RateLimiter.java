package org.machinecode.ratelimiter;
/*
Defines the contract (methods like acquireToken() and shutdown()) for all rate limiter implementations.

This uses the Strategy Pattern and adheres to DIP and ISP by letting clients depend on abstractions.
 */
public interface RateLimiter {

    void acquireToken() throws InterruptedException;
    void shutdown();
}
