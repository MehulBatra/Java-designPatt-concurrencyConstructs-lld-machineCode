package org.machinecode;


//Here's how it achieves the async-to-sync conversion:
//
//When asynchronousExecution is called, it immediately starts the asynchronous operation by calling
// super.asynchronousExecution(cb).
//Instead of returning immediately (which would be the asynchronous behavior),
// it enters a waiting state using signal.wait().
//The current thread (which called this method) will now wait at this point.
//When the asynchronous operation completes, it calls the done() method of our wrapper callback.
//This wrapper callback then notifies the waiting thread and sets the isDone flag.
//The notification causes the waiting thread to wake up. It checks the isDone flag and,
// seeing it's true, exits the while loop.
//Only now does the asynchronousExecution method return, making it appear synchronous to the caller.
public class AsyncToSyncConversion {
    public static void main(String[] args) throws InterruptedException {
        SynchronousExecutor executor = new SynchronousExecutor();
        executor.asynchronousExecution(() -> {
            System.out.println("I am done");
        });
        System.out.println("main thread exiting...");
    }

}


interface CallbackS {
    public void done();
}

class SynchronousExecutor extends Executor {

    @Override
    public void asynchronousExecution(CallbackS callbackS) throws InterruptedException {
        Object lock = new Object();
        final boolean [] status = new boolean[1];
        CallbackS cb = new CallbackS() {
            @Override
            public void done() {
                callbackS.done();
                synchronized (lock) {
                    lock.notify();
                    status[0] = true;
                }
            }
        };
        super.asynchronousExecution(callbackS);
        synchronized (lock) {
        while(!status[0]){
                lock.wait();

            }
        }
    }

    public void asynchronousExecution() {
        System.out.println("I am done");
    }
}



class Executor {
    public void asynchronousExecution(CallbackS callbackS) throws InterruptedException {
        Thread t=new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                callbackS.done();
            }

        });
    t.start();
    }
}
