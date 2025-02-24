//package org.machinecode.inmemorycache;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class QuickCodeLruCache {
//
//
//
//    public static void main(String[] args) throws InterruptedException {
//
//         LRUCacheMechanism lruCache = new LRUCacheMechanism(10);
//
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//
//        try {
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    lruCache.put(1, 1);
//                    System.out.println("Put (1, 1)");
//                }
//            });
//
//            executor.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    lruCache.put(2, 2);
//                    System.out.println("Put (2, 2)");
//                }
//
//            });
//
//
//            executor.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    System.out.println(" Get key 1: " + lruCache.get(1));
//                }
//            });
//
//        executor.submit(new Runnable() {
//
//            @Override
//            public void run() {
//
//                System.out.println(" Get key 2: " + lruCache.get(2));
//            }
//        });
//
//        }
//        finally {
//            executor.shutdown();
//            executor.awaitTermination(1, TimeUnit.HOURS);
//        }
//        }
//
//
//
//
//}
//
//
//class DllNode {
//    DllNode next;
//    DllNode prev;
//    int key;
//    int value;
//
//    public DllNode(int key, int value) {
//        this.key = key;
//        this.value = value;
//    }
//
//}
//
//class LRUCacheMechanism {
//    int capacity;
//    DllNode left;
//    DllNode right;
//    ConcurrentHashMap<Integer, DllNode> map;
//
//    public LRUCacheMechanism(int capacity) {
//        this.capacity = capacity;
//        map = new ConcurrentHashMap<>();
//        left = new DllNode(0, 0);
//        right = new DllNode(0, 0);
//        left.next = right;
//        right.prev = left;
//    }
//
//
//    public synchronized int get(int key) {
//        if(map.containsKey(key)) {
//            DllNode node = map.get(key);
//            delete(node);
//            insert(node);
//        }
//        return map.get(key).value;
//    }
//
//    public synchronized void put(int key, int value) {
//        if(map.containsKey(key)) {
//            DllNode node = map.get(key);
//            delete(node);
//        }
//        DllNode nnode = new DllNode(key, value);
//        map.put(key, nnode);
//        insert(nnode);
//        if(map.size() > capacity) {
//            DllNode lru = left.next;
//            map.remove(lru.key);
//            delete(lru);
//        }
//    }
//
//    public synchronized void delete(DllNode node) {
//        DllNode nodeprev = node.prev; // get the node prev and next
//        DllNode nodenext = node.next;
//
//        nodeprev.next = nodenext; // detach the actual node and connect with the next node
//        nodenext.prev = nodeprev;
//
//    }
//
//    public synchronized void insert(DllNode node) {
//        DllNode nodeprev = right.prev; // get the prev connect of latest node from the right side
//        DllNode nodenext = right; // get the actual righest node
//
//        nodeprev.next = node; // attach the second-latest node next with the upcoming node
//        nodenext.prev = node; // attach the most right dummy node with the upcoming node
//
//        node.prev = nodeprev; // vice versa direct the latest added node second last
//        node.next = nodenext; // vice versa direct the latest added node to the top right dummy node
//
//
//
//
//    }
//
//}
