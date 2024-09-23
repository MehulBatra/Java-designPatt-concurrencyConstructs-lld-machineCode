package org.machinecode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LruCacheMt {



    public static void main(String[] args) throws InterruptedException {

         LRUCacheMechanism lruCache = new LRUCacheMechanism(10);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        try {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    lruCache.put(1, 1);
                    System.out.println("Put (1, 1)");
                }
            });

            executor.submit(new Runnable() {

                @Override
                public void run() {
                    lruCache.put(2, 2);
                    System.out.println("Put (2, 2)");
                }

            });


            executor.submit(new Runnable() {

                @Override
                public void run() {
                    System.out.println(" Get key 1: " + lruCache.get(1));
                }
            });

        executor.submit(new Runnable() {

            @Override
            public void run() {

                System.out.println(" Get key 2: " + lruCache.get(2));
            }
        });

        }
        finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        }
        }




}


class Node{
    Node next;
    Node prev;
    int key;
    int value;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }

}

class LRUCacheMechanism {
    int capacity;
    Node left;
    Node right;
    ConcurrentHashMap<Integer, Node> map;

    public LRUCacheMechanism(int capacity) {
        this.capacity = capacity;
        map = new ConcurrentHashMap<>();
        left = new Node(0, 0);
        right = new Node(0, 0);
        left.next = right;
        right.prev = left;
    }


    public synchronized int get(int key) {
        if(map.containsKey(key)) {
            Node node = map.get(key);
            delete(node);
            insert(node);
        }
        return map.get(key).value;
    }

    public synchronized void put(int key, int value) {
        if(map.containsKey(key)) {
            Node node = map.get(key);
            delete(node);
        }
        Node nnode = new Node(key, value);
        map.put(key, nnode);
        insert(nnode);
        if(map.size() > capacity) {
            Node lru = left.next;
            map.remove(lru.key);
            delete(lru);
        }
    }

    public synchronized void delete(Node node) {
        Node nodeprev = node.prev; // get the node prev and next
        Node nodenext = node.next;

        nodeprev.next = nodenext; // detach the actual node and connect with the next node
        nodenext.prev = nodeprev;

    }

    public synchronized void insert(Node node) {
        Node nodeprev = right.prev; // get the prev connect of latest node from the right side
        Node nodenext = right; // get the actual righest node

        nodeprev.next = node; // attach the second-latest node next with the upcoming node
        nodenext.prev = node; // attach the most right dummy node with the upcoming node

        node.prev = nodeprev; // vice versa direct the latest added node second last
        node.next = nodenext; // vice versa direct the latest added node to the top right dummy node




    }

}
