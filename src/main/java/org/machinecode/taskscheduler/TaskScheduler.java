package org.machinecode.taskscheduler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
/*
Role & Responsibility:
Handles scheduling and executing tasks based on their dependencies.
It continuously checks which tasks are ready to run, submits them to an executor, and monitors completion.

SOLID Principles:

Single Responsibility: Dedicated to the scheduling logic and execution workflow.
Open/Closed: Can be extended with new scheduling policies without changing its core functionality.

Design Patterns & OOP:

Scheduler/Observer Pattern: Monitors task status and triggers execution when dependencies are met.
Inversion of Control: Retrieves tasks from the repository rather than managing task data itself, making it more modular.

How Does CountDownLatch Work?
Think of a relay race where the final runner waits for three teammates to finish their part before running.
CountDownLatch ensures the final runner starts only after all the teammates have finished.
 */
public class TaskScheduler {
    private final TaskRepository repository;
    private final ExecutorService executorService;

    public TaskScheduler(TaskRepository repository, ExecutorService executorService) {
        this.repository = repository;
        this.executorService = executorService;
    }

    public void schedule() throws InterruptedException {
        // Retrieve the map of all tasks from the repository.
        // The key is the task ID, and the value is the TaskT instance.
        Map<Integer, TaskT> tasks = repository.getTasks();

        // Retrieve the dependency graph from the repository.
        // Each key (task ID) maps to a set of task IDs that this task depends on.
        Map<Integer, Set<Integer>> dependencyGraph = repository.getDependencyGraph();

        // Create a thread-safe set to track which tasks have completed execution.
        Set<Integer> completedTasks = ConcurrentHashMap.newKeySet();

        // Create a thread-safe set to track which tasks have been scheduled already.
        Set<Integer> scheduledTasks = ConcurrentHashMap.newKeySet();

        // Create a CountDownLatch initialized with the total number of tasks.
        // This latch will be used to wait until all tasks have finished executing.
        CountDownLatch latch = new CountDownLatch(tasks.size());

        // Continue scheduling until the number of completed tasks equals the total number of tasks.
        while (completedTasks.size() < tasks.size()) {
            // Iterate over each task in the tasks map.
            for (Map.Entry<Integer, TaskT> entry : tasks.entrySet()) {
                // Extract the task ID from the current entry.
                int taskId = entry.getKey();
                // Extract the TaskT instance for the current task ID.
                TaskT task = entry.getValue();

                // If this task has already been scheduled, skip to the next task.
                if (scheduledTasks.contains(taskId)) {
                    continue;
                }

                // Retrieve the set of dependencies for this task.
                Set<Integer> dependencies = dependencyGraph.get(taskId);

                // Check if the task has no dependencies (dependencies == null)
                // or if all dependencies have been completed.
                if (dependencies == null || completedTasks.containsAll(dependencies)) {
                    // Mark the task as scheduled so that it is not scheduled again.
                    scheduledTasks.add(taskId);

                    // Submit the task to the executor for asynchronous execution.
                    executorService.submit(() -> {
                        try {
                            // Execute the task.
                            task.execute();
                            // Mark the task as completed after execution.
                            completedTasks.add(taskId);
                        } catch (Exception e) {
                            // Log any errors that occur during task execution.
                            System.err.println("Error executing task " + taskId + ": " + e.getMessage());
                        } finally {
                            // Decrement the CountDownLatch count regardless of success or failure.
                            latch.countDown();
                        }
                    });
                }
            }
            // Sleep briefly to avoid busy waiting and allow time for tasks to complete.
            Thread.sleep(100);
        }
        // Wait until the latch count reaches zero, meaning all tasks have finished.
        latch.await();
    }


    public void shutdown() {
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(60, TimeUnit.SECONDS)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
