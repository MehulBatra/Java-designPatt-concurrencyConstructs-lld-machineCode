package org.concurrency.fundamentals;

class Volatile {



    public static void main(String[] args) throws InterruptedException {

        Demo1.runExample();
    }
}


class Demo1{
// demo of volatile isnt thread safe
    static volatile int count = 0;

    public static void runExample() throws InterruptedException {

        int numThread = 10;
        Thread[] threads = new Thread[numThread];
        for(int i=0;i<numThread;i++){
            threads[i] = new Thread(new Runnable(){

                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++)
                        count++;
                }
            });
        }
        for(int i=0;i<numThread;i++){
            threads[i].start();
        }
        for(int i=0;i<numThread;i++){
            threads[i].join();
        }

        System.out.println("count = " + count);
    }

}
