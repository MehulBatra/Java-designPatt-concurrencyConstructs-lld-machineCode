package org.machinecode.taskscheduler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/*

Role & Responsibility:
Manages the storage of tasks and the mapping of their dependencies. It isolates the concerns of data management from scheduling or execution logic.

SOLID Principles:

Single Responsibility: Solely responsible for storing tasks and their dependency relationships.
Separation of Concerns: Keeps data management separate from the scheduling logic.
Design Patterns & OOP:

Encapsulation: Internal maps (tasks and dependency graph) are managed within the repository.
Repository Pattern: Abstracts the persistence and retrieval of task-related data.

 */
public class TaskRepository {
    private final Map<Integer, TaskT> tasks = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> dependencyGraph = new ConcurrentHashMap<>();

    public void addTask(TaskT task){
        tasks.put(task.getId(), task);
        dependencyGraph.putIfAbsent(task.getId(), new HashSet<>());
    }

    public void addDependency(int taskId, int dependencyId){
        if (!dependencyGraph.containsKey(taskId)) {
            dependencyGraph.put(taskId, new HashSet<>());
        }
        dependencyGraph.get(taskId).add(dependencyId);
    }

    public Map<Integer, TaskT> getTasks() {
        return tasks;
    }

    public Map<Integer, Set<Integer>> getDependencyGraph() {
        return dependencyGraph;
    }

}
