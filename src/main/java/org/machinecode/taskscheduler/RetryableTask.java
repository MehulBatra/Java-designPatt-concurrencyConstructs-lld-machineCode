package org.machinecode.taskscheduler;
/*

Role & Responsibility:
Implements the TaskT interface, encapsulating the actual work (via a Runnable action) along with retry logic.

SOLID Principles:

Single Responsibility: Focuses solely on executing its encapsulated action and managing retries.
Open/Closed: It’s designed for extension (you could create new types of tasks) without modifying its existing behavior.

Design Patterns & OOP:

Command Pattern: Acts as a concrete command holding both the action and metadata (e.g., ID, retry count).
Encapsulation: Internal state like retries and action are hidden from clients.

 */
public class RetryableTask implements TaskT{
    private final int id;
    private final int maxRetries;
    private int retries;
    private final Runnable task;

    public RetryableTask(int id, int maxRetries, Runnable task) {
        this.id = id;
        this.maxRetries = maxRetries;
        this.task = task;
        this.retries=0;
    }

    @Override
    public void execute() {
        while(retries<maxRetries) {
            try{
                task.run();
                return;
            }catch (Exception e) {
                retries++;
                if(retries>=maxRetries) {
                    throw new RuntimeException("Task " + id + " failed after " + maxRetries + " attempts", e);
                }
            }

        }
    }

    @Override
    public int getId() {
        return id;
    }
}
