package org.machinecode.inmemorycache;

/*
OOP Principle: Encapsulation & Cohesion
The Node class is a simple Plain Old Java Object (POJO) that encapsulates data (key, value)
 */
public class Node {
    Node next;
    Node prev;
    int key;
    int value;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
