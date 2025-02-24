package org.concurrency.multithreadingproblems;

import java.util.concurrent.CountDownLatch;

public class PrintNumbersInOrderMt {

    public static void main(String[] args) {

        PrintNumbers p = new PrintNumbers();
        PrintNumbersThread t1 = new PrintNumbersThread(p,"first");
        PrintNumbersThread t2 = new PrintNumbersThread(p,"second");
        PrintNumbersThread t3 = new PrintNumbersThread(p,"third");
        t3.start();
        t1.start();
        t2.start();
    }
}

class PrintNumbers {

    CountDownLatch latch1;
    CountDownLatch latch2;

    public PrintNumbers() {
        latch1 = new CountDownLatch(1);
        latch2 = new CountDownLatch(1);
    }

    public void printFirst(){
        System.out.println("First");
        latch1.countDown();
    }

    public void printSecond() throws InterruptedException {
        latch1.await();
        System.out.println("Second");
        latch2.countDown();
    }

    public void printThird() throws InterruptedException {
        latch2.await();
        System.out.println("Third");
    }

}

class PrintNumbersThread extends Thread {

    String input;
    PrintNumbers obj;

    public PrintNumbersThread(PrintNumbers obj, String input) {
        this.obj = obj;
        this.input=input;
    }

    public void run() {
        if(input.equals("first")){
            obj.printFirst();
        }
        else if(input.equals("second")){
            try {
                obj.printSecond();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                obj.printThird();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
