package org.machinecode.taskscheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
Role & Responsibility:
Acts as the entry point of the application. It initializes the repository, scheduler, and tasks, then orchestrates the overall workflow.

SOLID Principles:

Single Responsibility: Its sole purpose is to configure and kick off the scheduling process.
Separation of Concerns: Delegates task management and scheduling to dedicated classes.

Design Patterns & OOP:

Composition: Composes together the repository and scheduler to build the application workflow.
Application Layer: Serves as the coordination layer without embedding business logic.
 */
public class Main {

    public static void main(String[] args) {
        //
        TaskRepository taskRepository = new TaskRepository();
        ExecutorService executorService = Executors.newCachedThreadPool();

        TaskT task1 = new RetryableTask(1, 3, new Runnable() {
            @Override
            public void run() {
                System.out.println("task1");
                sleep(1000);
            }
        });

        TaskT task2 = new RetryableTask(2, 3, () -> {
            System.out.println("task2");
            sleep(1500);
        });

        TaskT task3 = new RetryableTask(3, 3, () -> {
            System.out.println("task3");
            sleep(500);
        });

        taskRepository.addTask(task1);
        taskRepository.addTask(task2);
        taskRepository.addTask(task3);
        taskRepository.addDependency(2, 1);
        taskRepository.addDependency(3, 2);

        TaskScheduler scheduler = new TaskScheduler(taskRepository, executorService);
        try {
            System.out.println("Starting workflow execution...");
            scheduler.schedule();
            System.out.println("Workflow execution completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Workflow interrupted: " + e.getMessage());
        } finally {
            scheduler.shutdown();
        }

    }
    private static void sleep( long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }
}
