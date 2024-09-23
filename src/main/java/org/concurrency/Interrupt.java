package org.concurrency;


class Demo{

    public static void main(String[] args) throws InterruptedException {
            InterruptSync.example();
    }
}



class InterruptSync {

    public static void example() throws InterruptedException {
        final Thread sleepyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("I am too sleepy... Let me sleep for an hour.");
                    Thread.sleep(1000 * 60 * 60); // this is a static method of thread class not limited to any
                    // instance, it makes the thread sleep which is currently working on operation
                } catch (InterruptedException e) {
                    System.out.println("The interrupt flag is cleard : " + Thread.interrupted() + " " + Thread.currentThread().isInterrupted());
                    // this will check the status if its being changed or not and setback to default
                    Thread.currentThread().interrupt();
                    System.out.println("Oh someone woke me up ! ");
                    System.out.println("The interrupt flag is set now : " + Thread.currentThread().isInterrupted() + " " + Thread.interrupted());
                    // this will check the status if being changed or and will not set back to default, will keep it
                    // the way it is

                    throw new RuntimeException(e);
                }


            }
        });

        sleepyThread.start(); // to run a parrallel thread with the main thread

        System.out.println("About to wake up the sleepy thread ...");
        sleepyThread.interrupt();
        System.out.println("Woke up sleepy thread ...");

        sleepyThread.join(); // make this instance of Thread class to finish with main thread

    }


}
