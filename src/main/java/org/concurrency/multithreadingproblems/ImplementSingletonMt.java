package org.concurrency.multithreadingproblems;

public class ImplementSingletonMt{
    public static void main(String[] args) {
        ImplementSingleton implementSingleton = ImplementSingleton.getInstance();
        implementSingleton.sayHello();

    }
}
//By using this pattern, you ensure that only one Singleton class object can exist.
// Anytime you need to use Singleton in your code, you call implementSingleton.getInstance(),
// which will always return the same object.
class ImplementSingleton {
    private volatile static ImplementSingleton instance; // stored in metaspace in jvm This area is shared among all threads.

    private ImplementSingleton() {}

    public static ImplementSingleton getInstance() {
        //Reduced initial load time of the application, as the object isn't created until it's needed.
        //Conservation of system resources if the instance ends up never being used.
        if (instance == null) { // Double lock
            synchronized (ImplementSingleton.class) {
                if (instance == null) { // helps make it lazy only loaded when the instance is actually called and
                    // found null
                    instance = new ImplementSingleton();
                }
            }
        }
        return instance;
    }
    public void sayHello() {
        System.out.println("Hello World!");
    }

}
