package org.concurrency.fundamentals;

class demo{
    
    public static void main(String[] args) throws InterruptedException {
      IncorrectSync.runExample();
      
    }
}

// The object the first thread synchronized on before going to sleep has been changed, and now it is attempting to
// call wait() on an entirely different object without having synchronized on it.

class IncorrectSync {
    
    
    Boolean flag = new Boolean(true);
    
    public void example() throws InterruptedException {
        
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {

                while(flag){
                    try{
                        System.out.println("First thread about to sleep");
                        Thread.sleep(5000);
                        System.out.println("Woke up and about to invoke wait()");
                        flag.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        
      Thread thread2 = new Thread(new Runnable() {
          @Override
          public void run() {
              
              flag = false;
              System.out.println("Boolean assignment done.");
              
          }
      });
      
      thread1.start();
      Thread.sleep(1000);
      thread2.start();
      thread1.join();
      thread2.join();
    }
    
    public static void runExample() throws InterruptedException {
        IncorrectSync incorrectSync = new IncorrectSync();
        incorrectSync.example();
    }
    
}