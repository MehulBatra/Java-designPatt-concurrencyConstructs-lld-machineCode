package org.machinecode;

import java.util.concurrent.Semaphore;

public class PrintNseriesOddEvenMt {
    public static void main(String[] args) {

        PrintMechanism printMechanism = new PrintMechanism(999999999);
        PrintDecider p1 = new PrintDecider(printMechanism,"zero");
        PrintDecider p2 = new PrintDecider(printMechanism,"even");
        PrintDecider p3 = new PrintDecider(printMechanism,"odd");

        p1.start();
        p2.start();
        p3.start();
    }
}

class PrintMechanism {
    int n;
    Semaphore zero, even, odd;

    public PrintMechanism(int n) {
        this.n = n;
        zero = new Semaphore(1);
        even = new Semaphore(0);
        odd = new Semaphore(0);
    }


    public void printZero() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            zero.acquire();
            System.out.print("0");
            (i%2 == 0? odd : even ).release();
        }
    }

    public void printEven() throws InterruptedException {
        for (int i = 2; i <=n; i+=2) {
            even.acquire();
            System.out.print(i);
            zero.release();
        }
    }

    public void printOdd() throws InterruptedException {
        for (int i = 1; i <=n; i+=2) {
            odd.acquire();
            System.out.print(i);
            zero.release();
        }
    }

}

class PrintDecider extends Thread {
    PrintMechanism print;
    String name;

    public PrintDecider(PrintMechanism print, String name) {
        this.print = print;
        this.name = name;
    }

    @Override
    public void run() {
        if(name.equals("zero")){
            try {
                print.printZero();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if(name.equals("even")){
            try {
                print.printEven();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                print.printOdd();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
