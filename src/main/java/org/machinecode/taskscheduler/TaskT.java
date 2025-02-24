package org.machinecode.taskscheduler;
/*
Role & Responsibility:
Provides an abstraction for a task. By declaring methods like execute() and getId(),
it defines a contract that any task must follow.

SOLID Principles:

Dependency Inversion: Higher-level modules (like the scheduler)
depend on this abstraction rather than concrete implementations.
Interface Segregation: The interface is focused and minimal.

Design Patterns & OOP:

Command Pattern: Each task (command) encapsulates a piece of behavior that can be executed.
Abstraction & Polymorphism: Clients can operate on any task implementation that conforms to TaskT.
 */

public interface TaskT {
    void execute();
    int getId();
}
