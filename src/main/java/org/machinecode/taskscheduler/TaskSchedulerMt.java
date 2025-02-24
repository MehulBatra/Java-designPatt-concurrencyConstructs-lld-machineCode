//package org.machinecode.taskscheduler;
//
//import java.util.*;
//import java.util.Queue;
//import java.util.concurrent.*;
//
//public class TaskSchedulerMt {
//
//    public static void main(String[] args) {
//        TaskManager taskManager = new TaskManager();
//
//        // Create tasks
//        Task task1 = new Task(1, 3, () -> {
//            System.out.println("Executing Task 1");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Task task2 = new Task(2, 3, () -> {
//            System.out.println("Executing Task 2");
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Task task3 = new Task(3, 3, () -> {
//            System.out.println("Executing Task 3");
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        // Add tasks to the task manager
//        taskManager.addTask(task1);
//        taskManager.addTask(task2);
//        taskManager.addTask(task3);
//
//        // Set up dependencies
//        taskManager.addTaskDependecy(2, 1); // Task 2 depends on Task 1
//        taskManager.addTaskDependecy(3, 2); // Task 3 depends on Task 2
//
//        try {
//            System.out.println("Starting workflow execution...");
//            taskManager.executeWorkflow();
//            System.out.println("Workflow execution completed.");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            taskManager.shutDown();
//        }
//    }
//}
//
//
//class Task{
//    int id;
//    int retries=0;
//    int maxRetries;
//    Runnable action;
//
//    public Task(int id, int maxRetries, Runnable action) {
//        this.id = id;
//        this.maxRetries = maxRetries;
//        this.action = action;
//
//    }
//
//    public void execute(){
//            while(retries<maxRetries){
//                try {
//                   action.run();
//                    return;
//                } catch (Exception e) {
//                    retries++;
//                    if(retries>=maxRetries){
//                        throw new RuntimeException(e + "Threshold of retry breached");
//                    }
//
//                }
//
//            }
//    }
//
//
//    public Integer getId() {
//        return id;
//    }
//}
//
//class TaskManager{
//
//    Map<Integer, Set<Integer>> taskDependecy = new ConcurrentHashMap<>();
//    Queue<Task> queue = new LinkedList<>();
//    ExecutorService executorService = Executors.newCachedThreadPool();
//
//
//
//    public void addTask(Task task){
//        queue.add(task);
//        taskDependecy.putIfAbsent(task.getId(), new HashSet<>());
//    }
//
//    public void addTaskDependecy(Integer taskId, Integer dependecyId){
//                taskDependecy.get(taskId).add(dependecyId);
//    }
//
//    public void executeWorkflow() throws InterruptedException {
//            Set<Integer> completedTaskIds = new ConcurrentSkipListSet<>();
//            CountDownLatch countDownLatch = new CountDownLatch(queue.size());
//            while(completedTaskIds.size()<queue.size()){
//                Task task = queue.poll();
//                Integer taskId = task.getId();
//                if(!completedTaskIds.contains(taskId)) {
////                    if (completedTaskIds.containsAll(taskDependecy.get(taskId))) {
//                        executorService.submit(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    task.execute();
//                                    completedTaskIds.add(taskId);
//                                    countDownLatch.countDown();
//                                } catch (Exception e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//
//                        });
//                    }
////                }
//
//                Thread.sleep(100);
//            }
//            countDownLatch.await();
//
//    }
//
//    public void shutDown(){
//            executorService.shutdown();
//            try {
//                if(executorService.awaitTermination(60, TimeUnit.SECONDS)){
//                    executorService.shutdownNow();
//                }
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//    }
//
//
//}
