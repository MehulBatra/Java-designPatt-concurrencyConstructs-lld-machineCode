package org.machinecode.ratelimiter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/*
TokenBucketRateLimiter:
Implements the token bucket algorithm with thread-safe token management.
It follows SRP (handling only token logic) and OCP (can be extended later).
It uses an explicit start() method to begin the refill task,
avoiding thread startup in the constructor.
 */
public class TokenBucketRateLimiter implements RateLimiter {
    private final int maxTokens;
    private int tokens;
    private final long refreshInterval;
    private ScheduledExecutorService scheduler;

    public TokenBucketRateLimiter(final int maxTokens, final long refreshInterval) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refreshInterval = refreshInterval;
    }

    public void start(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(()->{
            synchronized (this) {
                if(tokens < maxTokens) {
                    tokens++;
                    this.notifyAll();
                }
            }
        },refreshInterval,refreshInterval, TimeUnit.MILLISECONDS);
    }
    @Override
    public void acquireToken() throws InterruptedException {
        synchronized (this) {
            while(tokens==0){
                this.wait();
            }
            tokens--;
            System.out.println("Token granted to " + Thread.currentThread().getName() + " at " +
                    (System.currentTimeMillis() / 1000));
        }
    }
    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
