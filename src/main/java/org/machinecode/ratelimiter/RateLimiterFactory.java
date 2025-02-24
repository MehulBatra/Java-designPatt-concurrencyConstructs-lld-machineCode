package org.machinecode.ratelimiter;
/*
RateLimiterFactory:
Encapsulates the creation logic using the Factory Pattern.
It allows easy switching between implementations (e.g., TOKEN vs. LEAKY)
without changing client code, supporting OCP and encapsulation.
 */
public class RateLimiterFactory {

    private RateLimiterFactory() {}

    public static RateLimiter createRateLimiter(String type, int capacity, long refreshInterval) {
        if ("TOKEN".equalsIgnoreCase(type)) {
            TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(capacity, refreshInterval);
            tokenBucketRateLimiter.start();
            return tokenBucketRateLimiter;
        } else if ("LEAKY".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Leaky bucket rate limiter not yet implemented.");
        } else {
            throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        }
    }

}
