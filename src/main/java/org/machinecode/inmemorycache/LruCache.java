package org.machinecode.inmemorycache;

import java.util.concurrent.ConcurrentHashMap;

/*
OOP Principle: Encapsulation

Each caching mechanism encapsulates its own logic. For instance:

LRUCacheMechanism: Uses a doubly linked list (with dummy nodes) to track the least and most recently used elements.

Design Pattern: Strategy Pattern
Both classes implement the ICache interface. This allows the swapping of caching strategies dynamically. The caching algorithm becomes a “strategy” that is selected at runtime via the factory.
 */

public class LruCache implements CacheT {
    int capacity;
    Node left;
    Node right;
    ConcurrentHashMap<Integer, Node> map;

    public LruCache(final int capacity) {
        this.capacity = capacity;
        map = new ConcurrentHashMap<>();
        left = new Node(0,0);
        right = new Node(0,0);
        left.next = right;
        right.prev = left;
    }


    @Override
    public synchronized int get(int key) {
        if(map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
            insert(node);
            return node.value;
        }
        return -1;
    }

    @Override
    public synchronized void put(int key, int value) {
        if(map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
        }
        Node newNode = new Node(key, value);
        map.put(key, newNode);
        insert(newNode);
        if(map.size() >= capacity) {
            Node leastUsed = left.next;
            map.remove(leastUsed.key);
            remove(leastUsed);
        }
    }

    public synchronized void remove(Node node) {
        Node nodePrev = node.prev;
        Node nodeNext = node.next;
        nodePrev.next = nodeNext;
        nodeNext.prev=nodePrev;

//        dummy->a->b->c->d->dummy

    }

    public synchronized void insert(Node node) {
        Node nodePrev = right.prev;
        nodePrev.next = node;
        node.next = right;
        right.prev = node;
        node.prev = nodePrev;

    }

    @Override
    public String toString() {
        return "LRUCacheMechanism";
    }
}
