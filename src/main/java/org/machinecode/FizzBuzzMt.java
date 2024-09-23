package org.machinecode;

public class FizzBuzzMt {
    public static void main(String[] args) {
        FizzBuzzLogic fb = new FizzBuzzLogic(15);
        FizzBuzzRunner f1 = new FizzBuzzRunner(fb, "Fizz");
        FizzBuzzRunner f2 = new FizzBuzzRunner(fb,"Buzz");
        FizzBuzzRunner f3 = new FizzBuzzRunner(fb,"FizzBuzz");
        FizzBuzzRunner f4 = new FizzBuzzRunner(fb,"Number");

        f1.start();
        f2.start();
        f3.start();
        f4.start();
    }
}

class FizzBuzzLogic {


    int n;
    int currentCapacity = 1;

    public FizzBuzzLogic(int n) {

        this.n = n;
    }

    public synchronized void fizz() throws InterruptedException {
        while (currentCapacity <= n) {
            if (currentCapacity % 3 == 0 && currentCapacity % 5 != 0) {
                System.out.println("Fizz");
                currentCapacity++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void buzz() throws InterruptedException {
        while (currentCapacity <= n) {
            if (currentCapacity % 3 != 0 && currentCapacity % 5 == 0) {
                System.out.println("Buzz");
                currentCapacity++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void fizzBuzz() throws InterruptedException {
        while (currentCapacity <= n) {
            if (currentCapacity % 3 == 0 && currentCapacity % 5 == 0) {
                System.out.println("FizzBuzz");
                currentCapacity++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void printNumber() throws InterruptedException {
        while (currentCapacity < n) {
            if (currentCapacity % 3 != 0 && currentCapacity % 5 != 0) {
                System.out.println(currentCapacity);
                currentCapacity++;
                notifyAll();
            } else {
                wait();
            }
        }

    }
}
class FizzBuzzRunner extends Thread {

        FizzBuzzLogic fizzBuzzLogic;
        String methodName;

        public FizzBuzzRunner(FizzBuzzLogic fizzBuzzLogic, String methodName) {
            this.fizzBuzzLogic = fizzBuzzLogic;
            this.methodName = methodName;
        }

        public void run() {
            if ("Fizz".equals(methodName)) {
                try {
                    fizzBuzzLogic.fizz();
                } catch (Exception e) {
                }
            } else if ("Buzz".equals(methodName)) {
                try {
                    fizzBuzzLogic.buzz();
                } catch (Exception e) {
                }
            } else if ("FizzBuzz".equals(methodName)) {
                try {
                    fizzBuzzLogic.fizzBuzz();
                } catch (Exception e) {
                }
            } else if ("Number".equals(methodName)) {
                try {
                    fizzBuzzLogic.printNumber();
                } catch (Exception e) {
                }
            }

        }

    }

